package org.example.model;

data class NameOfAPerson(
    val firstname: String,
    val lastname: String){

    val displayName = "$firstname $lastname"
}