package com.islam.otgtask.pojo

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class Contact(var id :Int,var firstName: String, var lastName: String,
                   var phoneNumber: String,  var phoneCode: String,
                   var email: String, var address: String,var photoPath:String,
                   var longitude: Double, var latitude: Double) : Parcelable{
    constructor() : this(0,"","","","","","","",0.0,0.0)
}