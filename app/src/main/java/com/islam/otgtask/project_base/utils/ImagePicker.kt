package com.islam.otgtask.project_base.utils

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleObserver
import androidx.lifecycle.OnLifecycleEvent
import pl.aprilapps.easyphotopicker.DefaultCallback
import pl.aprilapps.easyphotopicker.EasyImage
import pl.aprilapps.easyphotopicker.EasyImage.handleActivityResult
import java.io.File


object ImagePicker : LifecycleObserver {


    var onPicked: ((imageFile: File?) -> Unit)? = {}
    fun pickImage(fragment: Fragment, onImagePicked: (imageFile: File?) -> Unit) {
        fragment.viewLifecycleOwner.lifecycle.addObserver(this)
        onPicked = onImagePicked
        fragment.childFragmentManager.beginTransaction()
                .add(ImagePickerFragment(), "ImagePickerFragment")
                .commitNow()

    }

    @OnLifecycleEvent(Lifecycle.Event.ON_DESTROY)
    fun disconnectListener() {
        onPicked = null
    }

    class ImagePickerFragment : Fragment() {


        override fun onCreate(savedInstanceState: Bundle?) {
            super.onCreate(savedInstanceState)
            retainInstance = true
            if (savedInstanceState == null)
                EasyImage.openGallery(this, EasyImage.REQ_PICK_PICTURE_FROM_GALLERY)
        }

        override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
            super.onActivityResult(requestCode, resultCode, data)
            handleActivityResult(requestCode, resultCode, data, activity!!, object : DefaultCallback() {
                override fun onImagePicked(imageFile: File?, source: EasyImage.ImageSource?, type: Int) {
                    onPicked?.invoke(imageFile)
                    fragmentManager?.beginTransaction()?.remove(this@ImagePickerFragment)?.commitNow()
                }

                override fun onImagePickerError(e: Exception?, source: EasyImage.ImageSource?, type: Int) {
                    super.onImagePickerError(e, source, type)
                    ActivityManager.showToast("Cannot Load the Image", Toast.LENGTH_SHORT)
                    fragmentManager?.beginTransaction()?.remove(this@ImagePickerFragment)?.commitNow()
                }

                override fun onCanceled(source: EasyImage.ImageSource?, type: Int) {
                    if (source == EasyImage.ImageSource.CAMERA) {
                        val photoFile = EasyImage.lastlyTakenButCanceledPhoto(context)
                        photoFile?.delete()
                    }
                    fragmentManager?.beginTransaction()?.remove(this@ImagePickerFragment)?.commitNow()
                }
            })
        }
    }
}