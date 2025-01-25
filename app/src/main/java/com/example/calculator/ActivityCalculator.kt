package com.example.calculator

import android.icu.text.DecimalFormat
import android.nfc.FormatException
import android.os.Bundle
import android.view.MotionEvent
import android.view.View
import android.view.View.OnClickListener
import android.view.View.OnTouchListener
import android.view.ViewGroup
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity

import com.example.calculator.databinding.ActivityCalculatorBinding
import java.lang.IllegalArgumentException
import kotlin.math.cos
import kotlin.math.pow
import kotlin.math.sin
import kotlin.math.sqrt
import kotlin.math.tan
import kotlin.text.StringBuilder

class ActivityCalculator: AppCompatActivity(), OnClickListener, OnTouchListener {

    private lateinit var binding: ActivityCalculatorBinding
    //ViewGroup: clase padre para todos los layout. Permite manejar genéricamente cualquier
    //contenedor que tenga hijos, porque todos los layouts heredan de ViewGroup.
    private  lateinit var viewGroup: ViewGroup
    private  lateinit var value1:String
    private  lateinit var value2:String
    private  var operationBasic: Button? = null
    private  var operationScientific: Boolean = false


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding= ActivityCalculatorBinding.inflate(layoutInflater) // accede al layout ligado
        val view :View = binding.root // obtiene la vista raíz
        setContentView(view) // fusiona la parte gráfica con la lógica
        viewGroup = binding.root // obtiene la vista raiz. Puede ser un View o ViewGroup
        buttonListener(viewGroup)
        resetParameters()
        binding.textView.text = "0"


    }

    //Guarda el estado actual de la actividad antes de que sea destruida(cambio de orientación)
    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putString("screen", binding.textView.text.toString()) //Guarda el valor de la pantalla
        outState.putString("value1", value1) //Guarda el valor actual
        outState.putString("value2", value2) //Guarda el valor actual
        outState.putString("operation", operationBasic?.id.toString()) // Guarda el id de la operación que esté seleccionada
    }

    //Recupera el estado de actividad después de ser destruida
    override fun onRestoreInstanceState(savedInstanceState: Bundle) {
        super.onRestoreInstanceState(savedInstanceState)
        binding.textView.text= savedInstanceState.getString("screen") //restaura el valor de la pantalla
        value1 = savedInstanceState.getString("value1") ?:"" //Restaura el valor 1
        value2 = savedInstanceState.getString("value2") ?:"" // Restaura el valor 2
        val idBtn:String = savedInstanceState.getString("operation") ?: ""
        operationBasic = findButton(idBtn) //Restaura el botón de operación seleccionado
    }

    private fun buttonListener(viewGroup: ViewGroup){

        // Recorre la vista pasada por parámetros
        for (i in 0 until viewGroup.childCount){
            val view: View = viewGroup.getChildAt(i) // guarda una vista de cada iteración
            //Si la vista es un botón
            if (view is Button){
                view.setOnClickListener(this)
                view.setOnTouchListener(this)
            //Si es otro contenedor de vista, es decir, cualquier layout. LLama al método de forma recursiva
            }else if(view is ViewGroup){
                buttonListener(view)
            }
        }
    }



    override fun onClick(v: View?) {

        when(v!!.id){

            // BOTÓN RESET
            R.id.btnAc ->{
                binding.textView.text="0"
                resetParameters()
            }

            //BOTÓN BORRAR
            R.id.btnRemove ->{

                if (operationBasic==null && value1.isNotEmpty()){
                    value1= value1.dropLast(1)
                    binding.textView.text=value1
                } else if(operationBasic!=null && value2.isNotEmpty()){
                    value2=value2.dropLast(1)
                    binding.textView.text=value2
                }

            }


            //BOTÓN PORCENTAJE
            R.id.btnPercentage ->{
                try {
                    operationScientific=true //activa operación científica
                    val percentageDouble:Double = binding.textView.text.toString().toDouble()/100 //divide por 100 y pasa a double el valor de la pantalla
                    val percentageString:String= formatDouble(percentageDouble) // lo pasa a String ya formateado
                    binding.textView.text= percentageString //saca el valor por pantalla ya formateado
                    updateValues() //actualiza valores
                }catch (e:IllegalArgumentException){
                    resetParameters()
                }
            }


            //BOTÓN SENO
            R.id.btnSen ->{
                try {
                    operationScientific=true //activa operación científica
                    val value: Double = binding.textView.text.toString().toDouble() // pasa a double al valor actual de la pantalla
                    val radians: Double = Math.toRadians(value) // lo pasa a radianes
                    val valueSin:Double = sin(radians) // calcula seno
                    binding.textView.text= formatDouble(valueSin) // saca el valor por pantalla formateado
                    updateValues() //actualiza valores
                }catch (e:IllegalArgumentException){
                    resetParameters()
                }
            }


            //BOTÓN TANGENTE
            R.id.btnTan->{
                try {
                    operationScientific=true //activa operación científica
                    val value: Double = binding.textView.text.toString().toDouble() //pasa a double el valor actual de la pantalla
                    val radians: Double = Math.toRadians(value) //pasa a radianes
                    val valueTan:Double = tan(radians) //calcula tangente
                    binding.textView.text= formatDouble(valueTan) // saca valor por pantalla
                    updateValues() // actualiza valores
                }catch (e:IllegalArgumentException){
                    resetParameters()
                }

            }

            //BOTÓN COSENO
            R.id.btnCos->{
                try {
                    operationScientific=true //activa operación científica
                    val value: Double = binding.textView.text.toString().toDouble() //pasa a double el valor actual de la pantalla
                    val radians: Double = Math.toRadians(value) //calcula radianes
                    val valueCos:Double = cos(radians) //calcula coseno
                    binding.textView.text= formatDouble(valueCos) //saca valor por pantalla
                    updateValues() //actualiza valores
                }catch (e:IllegalArgumentException){
                    resetParameters()
                }

            }

            //BOTÓN PI
            R.id.btnPi->{
                operationScientific=true //activa operación científica
                binding.textView.text= formatDouble(Math.PI)
                updateValues()
            }

            //BOTÓN RANDOM
            R.id.btnRandom->{
                operationScientific=true //activa operación científica
                binding.textView.text= formatDouble(Math.random())
                updateValues()
            }

            //BOTÓN MÁS-MENOS
            R.id.btnMoreLess->{

                try {
                    val numDouble:Double= binding.textView.text.toString().toDouble() //Pasa el valor actual de la pantalla a double
                    val numString:String= binding.textView.text.toString() //Pasa el valor a string
                    val numStringBuilder:StringBuilder = StringBuilder(numString) //Crea un StringBuilder
                    if (numDouble>0){
                        numStringBuilder.insert(0,"-") //añade el símbolo menos
                        binding.textView.text= numStringBuilder.toString()
                    }else if(numDouble<0){
                        numStringBuilder.deleteCharAt(0) //elimina el símbolo menos
                        binding.textView.text= numStringBuilder.toString()
                    }
                    updateValues() //actualiza valores ????
                }catch (e:IllegalArgumentException){
                    resetParameters()
                }

            }

            //BOTÓN EXPONENTE
            R.id.btnE->{
                try {
                    operationScientific=true //activa operación científica
                    val exponent:Double= binding.textView.text.toString().toDouble()
                    val pow :Double= 10.0.pow(exponent) //calcula exponente en base 10
                    binding.textView.text = formatDouble(pow) //lo saca por pantalla formateado
                    updateValues() //actualiza valores
                }catch (e:IllegalArgumentException){
                    resetParameters()
                }

            }

            //BOTÓN RAIZ CUADRADA
            R.id.btnSquare->{

                try {
                    operationScientific =true
                    val numDouble:Double= binding.textView.text.toString().toDouble() //Pasa el valor actual de la pantalla a double
                    val squareRoot:Double = sqrt(numDouble) // calcula raiz cuadrada
                    binding.textView.text= formatDouble(squareRoot) //lo saca por pantalla formateado
                    updateValues() //actualiza valores
                }catch (e:IllegalArgumentException){
                    resetParameters()
                }

            }

            //BOTÓN FACTORIAL
            R.id.btnFactorial->{

                try {
                    operationScientific =true
                    val numDouble:Double= binding.textView.text.toString().toDouble() //Pasa el valor actual de la pantalla a double
                    val factorial:Int= calculateFactorial(numDouble) //calcula factorial
                    binding.textView.text= factorial.toString()
                    updateValues() //actualiza valores
                }catch (e:FormatException){
                    binding.textView.text=e.message
                    resetParameters()

                }catch (e:IllegalArgumentException){
                    resetParameters()
                }
            }

            //BOTONES DE OPERACIONES
            R.id.btnDivision, R.id.btnMultiplication,
            R.id.btnSubtraction, R.id.btnAdd->{


                //Solo entra si value 1 está vacío y en la pantalla hay algún valor diferente a cero
                //Evita que se vuelva a reasignar antes de pulsar un botón de operación
                try {
                    operationScientific=false
                    val valueActual: Double = binding.textView.text.toString().toDouble()
                    if(value1.isEmpty() && valueActual!=0.0){
                        value1 = binding.textView.text.toString()
                    }
                    //Asigna la operación siempre y cuando exista un valor previo
                    if(value1.isNotEmpty()){
                        operationBasic = (v as Button)
                    }
                }catch (e:IllegalArgumentException){
                    resetParameters()
                }

            }



            R.id.btnEquals->{
                val result:Double

                if (value2.isNotEmpty()){
                    //Evalúa qué operación está seleccionada y operaen consecuencia
                    when(operationBasic?.id){
                        R.id.btnAdd->{
                            result = value1.toDouble() + value2.toDouble()
                            binding.textView.text= formatDouble(result)
                            resetParameters()
                        }
                        R.id.btnSubtraction->{
                            result = value1.toDouble() - value2.toDouble()
                            binding.textView.text= formatDouble(result)
                            resetParameters()

                        }
                        R.id.btnMultiplication->{
                            result = value1.toDouble() * value2.toDouble()
                            binding.textView.text= formatDouble(result)
                            resetParameters()

                        }
                        R.id.btnDivision->{
                            result = value1.toDouble() / value2.toDouble()
                            binding.textView.text= formatDouble(result)
                            resetParameters()
                        }
                    }
                }

            }


            //BOTONES NÚMEROS
            R.id.btnZero, R.id.btnOne, R.id.btnTwo,
            R.id.btnThree, R.id.btnFour, R.id.btnFive,
            R.id.btnSix, R.id.btnSeven, R.id.btnEight,R.id.btnNine->{

                //Para asignar el primer valor:
                if (operationBasic==null && !operationScientific){
                    value1+=(v as Button).text.toString()
                    binding.textView.text = value1
                //Para asignar el segundo valor
                } else if(operationBasic!=null && !operationScientific){
                    value2+=(v as Button).text.toString()
                    binding.textView.text = value2

                }

            }
        }
    }

    override fun onTouch(v: View?, event: MotionEvent?): Boolean {
        when(event?.action){
            MotionEvent.ACTION_DOWN->{
                v?.setBackgroundColor(resources.getColor(R.color.white))
            }
            MotionEvent.ACTION_UP->{
                if(v?.id == R.id.btnEquals){
                    v.setBackgroundColor(resources.getColor(R.color.orange))
                }else{
                    v?.setBackgroundColor(resources.getColor(R.color.grey))
                }


            }
        }
        return  false
    }

    private fun formatDouble(numDouble:Double):String{
        return when{
            //Si es entero y tiene poca longuitud
            numDouble%1==0.0 && numDouble.toString().length<=10->{
                numDouble.toLong().toString()
            }
            //Si es entero y es muy largo
            numDouble%1==0.0 ->{
                val decimalFormat = DecimalFormat("0.00E0")
                decimalFormat.format(numDouble).replace(",", ".")
            }
            //Si es decimal
            else->{
                val decimalFormat = DecimalFormat("#.###")
                decimalFormat.format(numDouble).replace(",", ".")
            }
        }
    }

    private  fun resetParameters(){
        operationBasic=null
        operationScientific=false
        value1=""
        value2=""
    }

    /**
     *Recupera el botón de operación pulsado 
     */
    private  fun findButton(id:String):Button?{
        return listOf(binding.btnAdd, binding.btnSubtraction, binding.btnDivision, binding.btnMultiplication)
            .find { it.id.toString() ==id }
    }

    /**
     * Actualiza value1 y value2
     *
     **/
    private fun updateValues(){

        if(operationBasic==null){
            value1= binding.textView.text.toString()
        }else{
            value2= binding.textView.text.toString()
        }
    }

    private fun calculateFactorial(num:Double):Int{
        if (num%1!=0.0 || num<0.0){ //Si tien decimales o es menor que 0
            throw FormatException("No es un número")
        }
        var result:Int=1
        for (i in 1..num.toInt()){
            result*=i
        }
        return result
    }

}