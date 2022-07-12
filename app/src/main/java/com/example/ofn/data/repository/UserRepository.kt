package com.example.ofn.data.repository

import android.graphics.Bitmap
import android.util.Base64
import com.example.ofn.data.dao.UserDao
import com.example.ofn.data.model.User
import java.io.ByteArrayOutputStream


class UserRepository {

    private val userDao: UserDao = UserDao()

    fun getImageData(bmp: Bitmap):String {
        val bao = ByteArrayOutputStream()
        bmp.compress(Bitmap.CompressFormat.PNG, 100, bao) // bmp is bitmap from user image file
        bmp.recycle()
        val byteArray: ByteArray = bao.toByteArray()
        val imageB64: String = Base64.encodeToString(byteArray, Base64.URL_SAFE)
        //  store & retrieve this string which is URL safe(can be used to store in FBDB) to firebase
        // Use either Realtime Database or Firestore
        return imageB64
    }

    fun insert_new_user(uid: String, email: String){
        var user = User(uid, email, "" ,"","","")
        this.userDao.addUser(user)
    }
}