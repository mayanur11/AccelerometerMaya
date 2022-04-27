package com.mayanurlestari.accelerometermaya

import android.graphics.Color
import android.hardware.Sensor
import android.hardware.SensorEvent
import android.hardware.SensorEventListener
import android.hardware.SensorManager
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.TextView
import androidx.appcompat.app.AppCompatDelegate

//TODO 1 mengimplemetasikan SensorEventListener
//Sensor Event Listener dapat menggunakan antarmuka untuk method callback yang menerima sensor
//saat ada notifikasi nilai sensor berubah atau nilai akurasi berubah
class MainActivity : AppCompatActivity(), SensorEventListener {

    private lateinit var sensorManager: SensorManager
    private lateinit var square: TextView

//TODO 2 meng-overide method (abstraksi 2 method) = onSensorChanged dan onAccuracyChanged
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // Keeps phone in light mode
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)

        square = findViewById(R.id.tv_square)

        setUpSensorStuff()
    }

    private fun setUpSensorStuff() {
        // Create the sensor manager
        //TODO 3 menyiapkan variabel sensor manager
        //Sensor manager menyediakan beberapa konstanta sensor yang digunakan untuk melaporkan keakuratan sensor,
        // menetapkan kecepatan akuisisi data, dan mengalibrasi sensor.

        sensorManager = getSystemService(SENSOR_SERVICE) as SensorManager

        // Specify the sensor you want to listen to
        //TODO 4 sensor yang akan digunakan (diakses) yaitu Type_Accelerometer
        sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER)?.also { accelerometer ->
            sensorManager.registerListener(
                this,
                accelerometer,
                SensorManager.SENSOR_DELAY_FASTEST,
                SensorManager.SENSOR_DELAY_FASTEST
            )
        }
    }

    override fun onSensorChanged(event: SensorEvent?) {
        //Tujuan dari on Sensor Changed adalah mengambil data
        // TODO 5 Saat sensor berubah, maka Sensor.TYPE_ACCELEROMETER akan berubah
        if (event?.sensor?.type == Sensor.TYPE_ACCELEROMETER) {
            //Log.d("Main", "onSensorChanged: sides ${event.values[0]} front/back ${event.values[1]} ")

            // Sides = Tilting phone left(10) and right(-10)
            val sides = event.values[0]

            // Up/Down = Tilting phone up(10), flat (0), upside-down(-10)
            val upDown = event.values[1]

            square.apply {
                rotationX = upDown * 3f
                rotationY = sides * 3f
                rotation = -sides
                translationX = sides * -10
                translationY = upDown * 10
                //TODO 7 Menampilan data rotasi yang diakses terhadap sensor perangkat fisik/emulator
                //TODO 6 Mengambil data sensor  (event) sesuai dengan dokumentasi bagaimana cara mengaksesnya
            }

            // Changes the colour of the square if it's completely flat
            val color = if (upDown.toInt() == 0 && sides.toInt() == 0) Color.GREEN else Color.RED
            square.setBackgroundColor(color)

            square.text = "up/down ${upDown.toInt()}\nleft/right ${sides.toInt()}"
        }
    }

    override fun onAccuracyChanged(sensor: Sensor?, accuracy: Int) {
        //on Accuracy Changed tujuannya menjaga performace dari aplikasi supaya aplikasi cepat tetap tidak boros terhadap batrai
        return
    }

    override fun onDestroy() {
        sensorManager.unregisterListener(this)
        super.onDestroy()
    }
}

