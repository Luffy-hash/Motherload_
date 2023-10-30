package com.example.chatm1.model

import java.util.Date

// En Kotlin, la Data cass peut être déclarée en une ligne. Il n'y a rien sd'autre à faire.
data class Message(val id:Int, val author:String,val msg:String,val date:Date)