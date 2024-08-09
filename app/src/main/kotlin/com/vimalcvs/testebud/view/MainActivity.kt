package com.vimalcvs.testebud.view

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.vimalcvs.testebud.theme.TesteBudTheme
import java.math.BigDecimal

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            TesteBudTheme {
                CalculatorApp()
            }
        }
    }
}


@Composable
fun CalculatorApp() {
    var displayText by remember { mutableStateOf("0") }
    var currentOperation by remember { mutableStateOf("") }
    var firstOperand by remember { mutableStateOf(BigDecimal.ZERO) }
    var secondOperand by remember { mutableStateOf(BigDecimal.ZERO) }

    Column(
        modifier = Modifier
            .fillMaxSize(),
    ) {
        TextField(
            value = displayText,
            onValueChange = { displayText = it },
            modifier = Modifier
                .fillMaxWidth()
                .weight(1f)
                .wrapContentHeight(align = Alignment.Bottom),
            textStyle = TextStyle(
                fontSize = 70.sp,
                fontWeight = FontWeight.Bold,
                textAlign = TextAlign.End
            ),
            colors = TextFieldDefaults.colors(
                focusedIndicatorColor = Color.Transparent,
                unfocusedIndicatorColor = Color.Transparent,
                disabledIndicatorColor = Color.Transparent,
                errorIndicatorColor = Color.Transparent,
                cursorColor = Color.Black,
                focusedContainerColor = Color.Transparent,
                unfocusedContainerColor = Color.Transparent,
            ),

            )

        CalculatorGrid(
            onNumberClick = { number ->
                displayText = if (displayText == "0") {
                    number.toString()
                } else {
                    displayText + number
                }
            },
            onOperatorClick = { operator ->
                if (currentOperation.isEmpty()) {
                    firstOperand = BigDecimal(displayText)
                    currentOperation = operator
                    displayText = "0"
                } else {
                    secondOperand = BigDecimal(displayText)
                    displayText =
                        calculate(firstOperand, secondOperand, currentOperation).toString()
                    currentOperation = operator
                    firstOperand = BigDecimal(displayText)
                }
            },
            onClearClick = {
                displayText = "0"
                currentOperation = ""
                firstOperand = BigDecimal.ZERO
                secondOperand = BigDecimal.ZERO
            },
            onEqualsClick = {
                if (currentOperation.isNotEmpty()) {
                    secondOperand = BigDecimal(displayText)
                    displayText =
                        calculate(firstOperand, secondOperand, currentOperation).toString()
                    currentOperation = ""
                }
            }
        )

        Spacer(modifier = Modifier.height(25.dp))
    }
}


@Composable
fun CalculatorGrid(
    onNumberClick: (Int) -> Unit,
    onOperatorClick: (String) -> Unit,
    onClearClick: () -> Unit,
    onEqualsClick: () -> Unit
) {
    val buttons = listOf(
        "C", "(", ")", "/",
        "7", "8", "9", "*",
        "4", "5", "6", "-",
        "1", "2", "3", "+",
        "0", ".", "%", "="
    )

    Column(
        modifier = Modifier
            .fillMaxWidth(1f)
            .padding(10.dp),
        verticalArrangement = Arrangement.SpaceEvenly
    ) {
        buttons.chunked(4).forEach { row ->
            Row(
                modifier = Modifier.fillMaxWidth(),

                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                row.forEach { button ->
                    Button(
                        onClick = {
                            when (button) {
                                "C" -> onClearClick()
                                "=" -> onEqualsClick()
                                in "0".."9" -> onNumberClick(button.toInt()) // Convert button text to Int
                                else -> onOperatorClick(button)
                            }
                        },
                        modifier = Modifier
                            .weight(1f)
                            .aspectRatio(1f)
                            .padding(4.dp)
                    ) {
                        Text(
                            button,
                            fontSize = 32.sp,
                            fontWeight = FontWeight.Bold,
                            textAlign = TextAlign.Center,

                            )
                    }
                }
            }
        }
    }
}


@Composable
fun CalculatorButton(
    text: String,
    onClick: () -> Unit
) {
    Button(
        onClick = onClick,
        modifier = Modifier
            .padding(4.dp),
        colors = ButtonDefaults.buttonColors(
            // backgroundColor = Color.White,
            //   contentColor = Color.Black
        )
    ) {
        Text(
            text = text,
            fontSize = 20.sp,
            fontWeight = FontWeight.Bold
        )
    }
}

fun calculate(operand1: BigDecimal, operand2: BigDecimal, operation: String): BigDecimal {
    return when (operation) {
        "+" -> operand1 + operand2
        "-" -> operand1 - operand2
        "*" -> operand1 * operand2
        "/" -> if (operand2 != BigDecimal.ZERO) operand1 / operand2 else BigDecimal.ZERO
        else -> BigDecimal.ZERO
    }
}

@Preview(showBackground = true)
@Composable
fun DefaultPreview() {
    CalculatorApp()
}