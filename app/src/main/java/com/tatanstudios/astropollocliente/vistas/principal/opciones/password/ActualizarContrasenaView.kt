package com.tatanstudios.astropollocliente.vistas.principal.opciones.password

import androidx.compose.animation.core.animateDpAsState
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import com.tatanstudios.astropollocliente.R
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.collectIsPressedAsState
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.imePadding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.lifecycle.viewmodel.compose.viewModel
import com.airbnb.lottie.compose.LottieAnimation
import com.airbnb.lottie.compose.LottieCompositionSpec
import com.airbnb.lottie.compose.LottieConstants
import com.airbnb.lottie.compose.animateLottieCompositionAsState
import com.airbnb.lottie.compose.rememberLottieComposition
import com.tatanstudios.astropollocliente.componentes.BarraToolbarColor
import com.tatanstudios.astropollocliente.componentes.BloqueTextFieldPassword
import com.tatanstudios.astropollocliente.componentes.CustomModal1Boton
import com.tatanstudios.astropollocliente.componentes.CustomToasty
import com.tatanstudios.astropollocliente.componentes.LoadingModal
import com.tatanstudios.astropollocliente.componentes.ToastType
import com.tatanstudios.astropollocliente.extras.TokenManager
import com.tatanstudios.astropollocliente.viewmodel.ActualizarPasswordViewModel
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch

@Composable
fun ActualizarPasswordScreen(navController: NavHostController,
                               viewModel: ActualizarPasswordViewModel = viewModel()) {

    val ctx = LocalContext.current

    val password by viewModel.passowrd.observeAsState("")

    val resultado by viewModel.resultado.observeAsState()
    val isLoading by viewModel.isLoading.observeAsState(false)

    val interactionSource = remember { MutableInteractionSource() }
    val isPressed by interactionSource.collectIsPressedAsState()
    val keyboardController = LocalSoftwareKeyboardController.current
    var isPasswordVisible by remember { mutableStateOf(false) } // Control de visibilidad de la contraseña


    // Definir el color del fondo al presionar
    val loginButtonColor = if (isPressed) {
        colorResource(id = R.color.colorRojo).copy(alpha = 0.8f) // más oscuro al presionar
    } else {
        colorResource(id = R.color.colorRojo)
    }
    // Animación de sombra
    val elevation by animateDpAsState(if (isPressed) 12.dp else 6.dp)
    val scope = rememberCoroutineScope() // Crea el alcance de coroutine

    // MODAL 1 BOTON
    var showModal1Boton by remember { mutableStateOf(false) }
    var modalMensajeString by remember { mutableStateOf("") }
    val tokenManager = remember { TokenManager(ctx) }
    var idusuario by remember { mutableStateOf("") }
    val texto4CaracteresMinimo = stringResource(R.string.minimo_4_caracteres)

    LaunchedEffect(Unit) {
        scope.launch {
            idusuario = tokenManager.idUsuario.first()
        }
    }

    // Animación y diseño
    val composition by rememberLottieComposition(LottieCompositionSpec.RawRes(R.raw.jsonpassword))
    val progress by animateLottieCompositionAsState(
        composition = composition,
        iterations = LottieConstants.IterateForever
    )

    Scaffold(
        topBar = {
            BarraToolbarColor(
                navController,
                stringResource(R.string.actualizar_contrasena),
                colorResource(R.color.colorRojo),
            )
        }
    ) { innerPadding ->

        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .verticalScroll(rememberScrollState())
                    .imePadding() // 👈 necesario para que el teclado no tape el campo
            ) {

                LottieAnimation(
                    composition = composition,
                    progress = { progress },
                    modifier = Modifier
                        .height(225.dp)
                        .align(Alignment.CenterHorizontally) // Centrado horizontalmente
                        .padding(top = 24.dp) // Espaciado superior si quieres
                )

                // Card de inicio de sesión
                Card(
                    modifier = Modifier
                        .padding(horizontal = 12.dp, vertical = 12.dp)
                        .fillMaxWidth(),
                    shape = RoundedCornerShape(25.dp),
                    elevation = CardDefaults.cardElevation(defaultElevation = 10.dp)
                ) {
                    Column(
                        modifier = Modifier
                            .background(Color.White)
                            .padding(10.dp)
                    ) {

                        Spacer(modifier = Modifier.height(6.dp))

                        Text(
                            text = stringResource(id = R.string.actualizar),
                            fontFamily = FontFamily(Font(R.font.arthura_regular)),
                            color = Color.Black,
                            fontSize = 18.sp,
                            modifier = Modifier
                                .fillMaxWidth(),
                            textAlign = TextAlign.Center
                        )

                        Spacer(modifier = Modifier.height(6.dp))

                        // Bloque para la contraseña
                        BloqueTextFieldPassword(
                            text = password,
                            onTextChanged = { newText -> viewModel.setPassword(newText) },
                            isPasswordVisible = isPasswordVisible,
                            onPasswordVisibilityChanged = { isPasswordVisible = it },
                            maxLength = 16
                        )


                        Spacer(modifier = Modifier.height(6.dp))

                        Button(
                            onClick = {
                                // Acción de login

                                keyboardController?.hide()

                                when {

                                    password.isBlank() -> {
                                        modalMensajeString = ctx.getString(R.string.password_es_requerido)
                                        showModal1Boton = true
                                    }

                                    password.length < 4 -> {
                                        modalMensajeString = texto4CaracteresMinimo
                                        showModal1Boton = true
                                    }
                                    else -> {
                                        viewModel.actualizarContrasenaRetrofit(idusuario)
                                    }
                                }

                            },
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(top = 32.dp, start = 24.dp, end = 24.dp)
                                .shadow(
                                    elevation = elevation, // Cambia la sombra cuando se presiona
                                    shape = RoundedCornerShape(25.dp)
                                ),
                            colors = ButtonDefaults.buttonColors(
                                containerColor = loginButtonColor,  // Cambia color al presionar
                                contentColor = colorResource(R.color.colorBlanco),
                            ),
                            interactionSource = interactionSource // Para detectar la interacción
                        ) {
                            Text(
                                text = stringResource(id = R.string.actualizar),
                                fontSize = 18.sp,
                                style = TextStyle(
                                    fontSize = 20.sp,
                                    fontWeight = FontWeight.Medium,
                                )
                            )
                        }

                        Spacer(modifier = Modifier.height(10.dp))
                    }


                    if(showModal1Boton){
                        CustomModal1Boton(showModal1Boton, modalMensajeString, onDismiss = {showModal1Boton = false})
                    }

                    if (isLoading) {
                        LoadingModal(isLoading = isLoading)
                    }

                    resultado?.getContentIfNotHandled()?.let { result ->
                        when (result.success) {

                            1 -> {
                                // CONTRASENA ACTUALIZADA
                                CustomToasty(
                                    ctx,
                                    stringResource(id = R.string.actualizado),
                                    ToastType.SUCCESS
                                )
                            }
                            else -> {
                                // Error, mostrar Toast
                                CustomToasty(
                                    ctx,
                                    stringResource(id = R.string.error_reintentar_de_nuevo),
                                    ToastType.ERROR
                                )
                            }
                        }
                    }
                } // end-card
            }
        }

    }


}

