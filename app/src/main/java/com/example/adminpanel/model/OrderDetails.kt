package com.example.adminpanel.model


import java.io.Serializable


data class OrderDetails(
    var userUid: String? = null,
    var userName: String? = null,
    var productNames: MutableList<String>? = null,
    var productPrices: MutableList<String>? = null,
    var productDis: MutableList<String>? = null,
    var productImages: MutableList<String>? = null,
    var discounts: MutableList<String>? = null,
    var productsLefts: MutableList<String>? = null,
    var deliveryCharges: MutableList<String>? = null,
    var productHighlights: MutableList<String>? = null,
    var productQuantity: MutableList<Int>? = null,
    var address: String? = null,
    var totalPrice: String? = null,
    var phoneNumber: String? = null,
    var orderAccepted: Boolean = false,
    var paymentReceived: Boolean = false,
    var itemPushKey: String? = null,
    var currentTime: Long = 0
) : Serializable

/*class OrderDetails() : Serializable {

        var userUid: String? = null
        var userName: String? = null
        var productNames: MutableList<String>? = null
        var productPrices: MutableList<String>? = null
        var productDis: MutableList<String>? = null
        var productImages: MutableList<String>? = null
        var discounts: MutableList<String>? = null
        var productsLefts: MutableList<String>? = null
        var deliveryCharges: MutableList<String>? = null
        var productHighlights: MutableList<String>? = null
        var productQuantity: MutableList<Int>? = null
        var address: String? = null
        var totalPrice: String? = null
        var phoneNumber: String? = null
        var orderAccepted: Boolean = false
        var paymentReceived: Boolean = false
        var itemPushKey: String? = null
        var currentTime: Long = 0

        // ✅ Secondary constructor for reading from Parcel
        constructor(parcel: Parcel) : this() {
            userUid = parcel.readString()
            userName = parcel.readString()
            productNames = parcel.createStringArrayList()
            productPrices = parcel.createStringArrayList()
            productDis = parcel.createStringArrayList()
            productImages = parcel.createStringArrayList()
            discounts = parcel.createStringArrayList()
            productsLefts = parcel.createStringArrayList()
            deliveryCharges = parcel.createStringArrayList()
            productHighlights = parcel.createStringArrayList()
            productQuantity = mutableListOf<Int>().apply {
                parcel.readList(this, Int::class.java.classLoader)
            }
            address = parcel.readString()
            totalPrice = parcel.readString()
            phoneNumber = parcel.readString()
            orderAccepted = parcel.readByte() != 0.toByte()
            paymentReceived = parcel.readByte() != 0.toByte()
            itemPushKey = parcel.readString()
            currentTime = parcel.readLong()
        }

         fun writeToParcel(parcel: Parcel, flags: Int) {
            parcel.writeString(userUid)
            parcel.writeString(userName)
            parcel.writeStringList(productNames)
            parcel.writeStringList(productPrices)
            parcel.writeStringList(productDis)
            parcel.writeStringList(productImages)
            parcel.writeStringList(discounts)
            parcel.writeStringList(productsLefts)
            parcel.writeStringList(deliveryCharges)
            parcel.writeStringList(productHighlights)
            parcel.writeList(productQuantity)
            parcel.writeString(address)
            parcel.writeString(totalPrice)
            parcel.writeString(phoneNumber)
            parcel.writeByte(if (orderAccepted) 1 else 0)
            parcel.writeByte(if (paymentReceived) 1 else 0)
            parcel.writeString(itemPushKey)
            parcel.writeLong(currentTime)
        }

         fun describeContents(): Int = 0

        companion object CREATOR : Parcelable.Creator<OrderDetails> {
            override fun createFromParcel(parcel: Parcel): OrderDetails {
                return OrderDetails(parcel)
            }

            override fun newArray(size: Int): Array<OrderDetails?> {
                return arrayOfNulls(size)
            }
        }
    }*/
