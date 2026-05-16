package ua.opu.mylibrary.data

import java.time.Year

/**
 * Модель одної книги.
 *
 * У цій лабораторній роботі дані зберігаються тільки у пам'яті застосунку,
 * тобто у звичайному списку всередині ViewModel. База даних поки не використовується.
 *
 * Поле id потрібне для навігації: коли користувач натискає на місце у списку,
 * на екран деталей передається тільки id, а ViewModel уже знаходить об'єкт у списку.
 */
data class Book(
    val id: Int,
    val name: String,
    val author: String,
    val year: Year,
    val rating: Float
)