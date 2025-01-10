package com.laorencel.ui.test.kt

open class Person( var name: String?,  var age: Int?){

}
class Student(name:String?, age: Int?,var className: String?) :
    Person(name, age)
