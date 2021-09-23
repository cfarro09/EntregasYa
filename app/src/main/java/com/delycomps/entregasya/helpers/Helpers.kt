package com.delycomps.entregasya.helpers


import android.app.Activity
import android.content.Context
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.location.Location
import android.os.Build
import android.os.Environment
import android.widget.Toast
import androidx.core.content.ContextCompat
import com.delycomps.entregasya.model.LocationDTO
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import java.io.File
import java.io.FileOutputStream
import java.io.OutputStream

public object Helpers {

    fun showToast(context: Context, message: String) {
        Toast.makeText(context, message, Toast.LENGTH_LONG).show()
    }

    fun verifyPermission(context: Context, perm: String, code : Int): Boolean {
        var res = false
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            val permission: Int = ContextCompat.checkSelfPermission(
                context, perm
            )
            if (permission != PackageManager.PERMISSION_GRANTED) {
                if ((context as Activity).shouldShowRequestPermissionRationale(perm)) {
                    showToast(context, "Debe aceptar el permiso para usar todas las funcionalidades de la aplicaicón.")
                } else {
                    context.requestPermissions(arrayOf(perm), code)
                }
            } else
                res = true
        }
        return res
    }

    fun saveBitmap(bmp: Bitmap): File? {
        val extStorageDirectory: String =
            Environment.getExternalStorageDirectory().toString()
        var outStream: OutputStream? = null
        // String temp = null;
        var file = File(extStorageDirectory, "temp.png")
        if (file.exists()) {
            file.delete()
            file = File(extStorageDirectory, "temp.png")
        }
        try {
            outStream = FileOutputStream(file)
            bmp.compress(Bitmap.CompressFormat.PNG, 100, outStream)
            outStream.flush()
            outStream.close()
        } catch (e: Exception) {
            e.printStackTrace()
            return null
        }
        return file
    }
}