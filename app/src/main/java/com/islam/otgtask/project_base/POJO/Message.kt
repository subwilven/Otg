package com.islam.otgtask.project_base.POJO

import android.content.Context

class Message {
    var text: String = ""
    var resource: Int = -1

    constructor(text: String) {
        this.text = text
    }

    constructor(resource: Int) {
        this.resource = resource
    }

    fun getValue(context: Context?): String {
        if (resource == -1)
            return text
        else
            return context?.getString(resource) ?: ""
    }


}