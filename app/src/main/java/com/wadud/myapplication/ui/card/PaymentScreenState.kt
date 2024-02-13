package com.wadud.myapplication.ui.card

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.focus.FocusRequester
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class PaymentScreenState {

    var cardNumber by mutableStateOf("")
        private set

    var expirationDate by mutableStateOf("")
        private set

    var cvv by  mutableStateOf("")
        private set

    var cardName by mutableStateOf("")
        private set

    var isButtonEnabled by mutableStateOf(false)
        private set

    var isExpirationDateError by mutableStateOf(false)
        private set

    val cardNumberFocusRequester =  FocusRequester()
    val holderNameFocusRequester = FocusRequester()
    val expirationDateFocusRequester = FocusRequester()
    val cvvFocusRequester =  FocusRequester()

    fun updateCardNumber(enteredCardNumber: String){
        cardNumber = enteredCardNumber
        updateButtonState()
    }

    fun cardEntryComplete() : Boolean{
        return  cardNumber.length == 16
    }

    fun expiryDateEntryComplete():Boolean{
        return expirationDate.length == 4
    }

    fun cvvEntryComplete():Boolean{
        return cvv.length == 3
    }
    
    fun updateCardName(enteredCardName : String){
        cardName = enteredCardName
        updateButtonState()
    }

    fun updateExpirationDate(enteredExpirationDate : String){
        expirationDate = enteredExpirationDate
        isExpirationDateError = !isDateValid()
        updateButtonState()
    }

    fun updateCvv(enteredCvv: String){
        cvv = enteredCvv
        updateButtonState()
    }

    private fun isDateValid(): Boolean {
        if (expirationDate.isBlank() || expirationDate.length < 4) return true
        val dateFormat = SimpleDateFormat("MMyy", Locale.getDefault())
        return try {
            val currentDate = Date()
            val inputDate = dateFormat.parse(expirationDate)
            println(currentDate)
            println(inputDate)
            inputDate.after(currentDate)
        } catch (e: Exception) {
            false
        }
    }


    private fun updateButtonState() {
        isButtonEnabled = cardEntryComplete() && cvvEntryComplete() && isDateValid() && cardName.trim().isNotEmpty() && expirationDate.trim().isNotEmpty()
    }
}

@Composable
fun rememberPaymentScreenInputState(): PaymentScreenState = remember { PaymentScreenState() }