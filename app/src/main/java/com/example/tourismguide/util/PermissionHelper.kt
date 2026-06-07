package com.example.tourismguide.util

import android.Manifest
import android.app.Activity
import android.content.Context
import android.content.pm.PackageManager
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat

object PermissionHelper {
    private const val LOCATION_REQUEST = 1001
    private const val CAMERA_REQUEST = 1002
    private const val PHONE_REQUEST = 1003

    fun requestLocationPermissions(activity: Activity, onGranted: () -> Unit, onDenied: () -> Unit) {
        if (checkPermission(activity, Manifest.permission.ACCESS_FINE_LOCATION)) {
            onGranted()
        } else {
            ActivityCompat.requestPermissions(activity, arrayOf(Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION), LOCATION_REQUEST)
            onDenied()
        }
    }

    fun requestCameraPermission(activity: Activity, onGranted: () -> Unit, onDenied: () -> Unit) {
        requestSingle(activity, Manifest.permission.CAMERA, CAMERA_REQUEST, onGranted, onDenied)
    }

    fun requestPhonePermission(activity: Activity, onGranted: () -> Unit, onDenied: () -> Unit) {
        requestSingle(activity, Manifest.permission.CALL_PHONE, PHONE_REQUEST, onGranted, onDenied)
    }

    fun checkPermission(context: Context, permission: String): Boolean =
        ContextCompat.checkSelfPermission(context, permission) == PackageManager.PERMISSION_GRANTED

    private fun requestSingle(activity: Activity, permission: String, requestCode: Int, onGranted: () -> Unit, onDenied: () -> Unit) {
        if (checkPermission(activity, permission)) {
            onGranted()
        } else {
            ActivityCompat.requestPermissions(activity, arrayOf(permission), requestCode)
            onDenied()
        }
    }
}
