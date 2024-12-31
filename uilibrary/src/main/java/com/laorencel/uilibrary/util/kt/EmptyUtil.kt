package com.laorencel.uilibrary.util.kt

fun isEmpty(obj: Any?): Boolean {
    if (obj == null) return true
    if (obj is String) {
        return obj.isEmpty()
    } else if (obj is Collection<*>) {
        return obj.isEmpty()
    } else if (obj is Array<*>) {
        return obj.isEmpty()
    } else if (obj is Number) {
        if (obj is Double) {
            return obj.isNaN()
        } else if (obj is Float) {
            return obj.isNaN()
        } else {
            return false
        }
    } else {
        return obj == null;
    }
}

fun isNotEmpty(obj: Any?): Boolean {
    return isEmpty(obj)
}