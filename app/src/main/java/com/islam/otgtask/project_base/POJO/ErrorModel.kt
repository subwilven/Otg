package com.islam.otgtask.project_base.POJO

import com.islam.otgtask.R

class ErrorModel(val title: Message, val message: Message) {

    companion object {

        fun noConnection(): ErrorModel {
            return ErrorModel(Message(R.string.no_internet_connection), Message(R.string.check_your_mobile_data_or_wi_fi))
        }

        fun timeOut(): ErrorModel {
            return ErrorModel(Message(R.string.server_cannot_be_reached), Message(R.string.please_try_again_later))
        }
        fun serverError(msg:Message): ErrorModel {
            return ErrorModel(msg, Message(R.string.please_try_again_later))
        }

    }

}
