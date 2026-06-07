package com.example.tourismguide.util

import android.graphics.Bitmap
import android.net.Uri
import android.widget.ImageView
import coil.load
import com.example.tourismguide.R
import com.google.firebase.storage.FirebaseStorage
import kotlinx.coroutines.tasks.await
import java.io.ByteArrayOutputStream

object ImageUtils {
    fun compressBitmap(bitmap: Bitmap, maxWidth: Int, maxHeight: Int, quality: Int): Bitmap {
        val ratio = minOf(maxWidth.toFloat() / bitmap.width, maxHeight.toFloat() / bitmap.height, 1f)
        val width = (bitmap.width * ratio).toInt()
        val height = (bitmap.height * ratio).toInt()
        return Bitmap.createScaledBitmap(bitmap, width, height, true).also {
            ByteArrayOutputStream().use { stream -> it.compress(Bitmap.CompressFormat.JPEG, quality, stream) }
        }
    }

    suspend fun uploadToFirebaseStorage(uri: Uri, path: String): Result<String> = try {
        val ref = FirebaseStorage.getInstance().reference.child(path)
        ref.putFile(uri).await()
        Result.success(ref.downloadUrl.await().toString())
    } catch (exception: Exception) {
        Result.failure(exception)
    }

    fun loadWithCoil(imageView: ImageView, url: String?, placeholder: Int = R.drawable.ic_placeholder) {
        imageView.load(url) {
            placeholder(placeholder)
            error(R.drawable.ic_error)
            crossfade(true)
        }
    }
}
