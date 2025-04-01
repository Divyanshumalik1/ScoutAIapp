package com.example.scoutai

import android.content.Context
import android.hardware.Sensor
import android.hardware.SensorEvent
import android.hardware.SensorEventListener
import android.hardware.SensorManager

class OrientationSensor(private val context: Context) {

    private var sensorManager: SensorManager = context.getSystemService(Context.SENSOR_SERVICE) as SensorManager
    private var rotationMatrix = FloatArray(9)
    private var orientationValues = FloatArray(3)
    private var gravity = FloatArray(3)
    private var geomagnetic = FloatArray(3)

    private var sensorEventListener: SensorEventListener? = null

    private var lastAzimuth: Float = 0f
    private var lastPitch: Float = 0f
    private var lastRoll: Float = 0f

    fun startListening(onOrientationChanged: (Float, Float, Float) -> Unit) {
        sensorEventListener = object : SensorEventListener {
            override fun onSensorChanged(event: SensorEvent?) {
                if (event != null) {
                    if (event.sensor.type == Sensor.TYPE_ACCELEROMETER) {
                        System.arraycopy(event.values, 0, gravity, 0, event.values.size)
                    } else if (event.sensor.type == Sensor.TYPE_MAGNETIC_FIELD) {
                        System.arraycopy(event.values, 0, geomagnetic, 0, event.values.size)
                    }

                    if (gravity.isNotEmpty() && geomagnetic.isNotEmpty()) {
                        val success = SensorManager.getRotationMatrix(rotationMatrix, null, gravity, geomagnetic)
                        if (success) {
                            SensorManager.getOrientation(rotationMatrix, orientationValues)
                            val azimuth = Math.toDegrees(orientationValues[0].toDouble()).toFloat()
                            val pitch = Math.toDegrees(orientationValues[1].toDouble()).toFloat()
                            val roll = Math.toDegrees(orientationValues[2].toDouble()).toFloat()

                            // Thresholds to prevent frequent updates
                            val azimuthChange = Math.abs(azimuth - lastAzimuth)
                            val pitchChange = Math.abs(pitch - lastPitch)
                            val rollChange = Math.abs(roll - lastRoll)

                            if (azimuthChange > 5f || pitchChange > 5f || rollChange > 5f) {
                                lastAzimuth = azimuth
                                lastPitch = pitch
                                lastRoll = roll
                                onOrientationChanged(azimuth, pitch, roll)
                            }
                        }
                    }
                }
            }

            override fun onAccuracyChanged(sensor: Sensor?, accuracy: Int) {}
        }

        val accelerometer = sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER)
        val magnetometer = sensorManager.getDefaultSensor(Sensor.TYPE_MAGNETIC_FIELD)
        sensorManager.registerListener(sensorEventListener, accelerometer, SensorManager.SENSOR_DELAY_UI)
        sensorManager.registerListener(sensorEventListener, magnetometer, SensorManager.SENSOR_DELAY_UI)
    }


    fun stopListening() {
        sensorManager.unregisterListener(sensorEventListener)
    }
}
