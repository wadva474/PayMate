package com.wadud.myapplication.ui.card

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.wadud.myapplication.R
import com.wadud.myapplication.ui.CardNumberMask
import com.wadud.myapplication.ui.EmptyComposable
import com.wadud.myapplication.ui.ExpirationDateMask
import kotlinx.coroutines.launch


@OptIn(ExperimentalComposeUiApi::class)
@Composable
@Preview(showBackground = true)
fun PaymentScreen(
    modifier: Modifier = Modifier,
    state: PaymentScreenState = rememberPaymentScreenInputState()
) {
    val keyboardController = LocalSoftwareKeyboardController.current
    val coroutineScope = rememberCoroutineScope()

    DisposableEffect(Unit) {
        state.cardNumberFocusRequester.requestFocus()
        onDispose { }
    }

    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(12.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Top
    ) {

        OutlinedTextField(
            value = state.cardNumber,
            onValueChange = {
                state.updateCardNumber(it)
                if (state.cardEntryComplete()) state.holderNameFocusRequester.requestFocus()
            },
            placeholder = { Text("**** **** **** ****") },
            label = { Text(stringResource(R.string.card_number)) },
            maxLines = 1,
            visualTransformation = CardNumberMask(),
            keyboardOptions = KeyboardOptions(
                keyboardType = KeyboardType.Number,
                imeAction = ImeAction.Next
            ),
            singleLine = true,
            modifier = Modifier
                .padding(6.dp)
                .fillMaxWidth()
                .focusRequester(state.cardNumberFocusRequester)
        )

        OutlinedTextField(
            value = state.cardName,
            onValueChange = { state.updateCardName(it) },
            label = { Text(stringResource(R.string.card_holder_name)) },
            maxLines = 1,
            keyboardOptions = KeyboardOptions(
                keyboardType = KeyboardType.Text,
                imeAction = ImeAction.Next
            ),
            singleLine = true,
            modifier = Modifier
                .padding(6.dp)
                .fillMaxWidth()
                .focusRequester(state.holderNameFocusRequester)
        )

        Row(
            modifier = Modifier
                .padding(vertical = 6.dp)
                .fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {

            OutlinedTextField(
                value = state.expirationDate,
                onValueChange = {
                    state.updateExpirationDate(it)
                    if (state.expiryDateEntryComplete()) state.cvvFocusRequester.requestFocus()
                },
                label = { Text(stringResource(R.string.expiration_date)) },
                isError = state.isExpirationDateError,
                supportingText = {
                    if (state.isExpirationDateError) {
                        Text(text = stringResource(R.string.invalid_date))
                    } else {
                        EmptyComposable()
                    }
                },
                visualTransformation = ExpirationDateMask(),
                keyboardOptions = KeyboardOptions(
                    keyboardType = KeyboardType.Number,
                    imeAction = ImeAction.Next
                ),
                singleLine = true,
                modifier = Modifier
                    .padding(6.dp)
                    .weight(1f)
                    .focusRequester(state.expirationDateFocusRequester)
            )

            OutlinedTextField(
                value = state.cvv,
                onValueChange = {
                    state.updateCvv(it)
                    if (state.cvvEntryComplete()) {
                        coroutineScope.launch {
                            keyboardController?.hide()
                        }
                    }
                },
                label = { Text(stringResource(R.string.cvv)) },
                keyboardOptions = KeyboardOptions(
                    keyboardType = KeyboardType.Number,
                    imeAction = ImeAction.Done
                ),
                supportingText ={
                   Text(text = stringResource(R.string.check_back_of_your_card))
                },
                singleLine = true,
                modifier = Modifier
                    .padding(6.dp)
                    .weight(1f)
                    .focusRequester(state.cvvFocusRequester)
            )
        }

        Button(
            onClick = {

            },
            enabled = state.isButtonEnabled,
            modifier = Modifier
                .padding(8.dp)
                .fillMaxWidth()
        ) {
            Text(stringResource(R.string.pay_now))
        }
    }
}
