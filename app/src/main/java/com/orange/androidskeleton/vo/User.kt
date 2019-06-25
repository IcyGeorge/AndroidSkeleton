package com.orange.androidskeleton.vo

import androidx.room.Entity
import com.google.gson.annotations.SerializedName

@Entity(primaryKeys = ["id"])
data class User(
    @field:SerializedName("id")
    val id: String,
    @field:SerializedName("employee_name")
    val name: String?,
    @field:SerializedName("employee_salary")
    val salary: String?,
    @field:SerializedName("employee_age")
    val age: String?,
    @field:SerializedName("profile_image")
    val imageUrl: String?
)
