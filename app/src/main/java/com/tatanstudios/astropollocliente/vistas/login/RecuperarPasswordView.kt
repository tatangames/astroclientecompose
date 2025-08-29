package com.tatanstudios.astropollocliente.vistas.login

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
import androidx.navigation.navOptions
import com.airbnb.lottie.compose.LottieAnimation
import com.airbnb.lottie.compose.LottieCompositionSpec
import com.airbnb.lottie.compose.LottieConstants
import com.airbnb.lottie.compose.animateLottieCompositionAsState
import com.airbnb.lottie.compose.rememberLottieComposition
import com.tatanstudios.astropollocliente.componentes.BloqueTextFieldCorreo
import com.tatanstudios.astropollocliente.componentes.CustomToasty
import com.tatanstudios.astropollocliente.componentes.LoadingModal
import com.tatanstudios.astropollocliente.componentes.OtpTextField
import com.tatanstudios.astropollocliente.componentes.ToastType
import com.tatanstudios.astropollocliente.model.rutas.Routes
import com.tatanstudios.astropollocliente.viewmodel.EnviarCorreoApiViewModel
import com.tatanstudios.astropollocliente.viewmodel.VerificarCodigoCorreoViewModel
import kotlinx.coroutines.launch

@Composable
fun RecuperarPasswordScreen(navController: NavHostController,
                            viewModel: EnviarCorreoApiViewModel = viewModel(),
                            viewModelVerificar: VerificarCodigoCorreoViewModel = viewModel()
) {
    val ctx = LocalContext.current
    val correo by viewModel.correo.observeAsState("")

    val resultado by viewModel.resultado.observeAsState()
    val isLoading by viewModel.isLoading.observeAsState(false)

    val resultadoVerificar by viewModelVerificar.resultado.observeAsState()
    val isLoadingVerificar by viewModelVerificar.isLoading.observeAsState(false)

    val interactionSource = remember { MutableInteractionSource() }
    val isPressed by interactionSource.collectIsPressedAsState()
    val keyboardController = LocalSoftwareKeyboardController.current

    // Definir el color del fondo al presionar
    val loginButtonColor = if (isPressed) {
        colorResource(id = R.color.colorRojo).copy(alpha = 0.8f) // más oscuro al presionar
    } else {
        colorResource(id = R.color.colorRojo)
    }
    // Animación de sombra
    val elevation by animateDpAsState(if (isPressed) 12.dp else 6.dp)
    val scope = rememberCoroutineScope() // Crea el alcance de coroutine

    val textoCorreoRequerido = stringResource(R.string.correo_es_requerido)
    val textoCorreoInvalido = stringResource(R.string.correo_ingresado_no_es_valido)
    val textoCodigoEnviado = stringResource(R.string.codigo_enviado)
    val textoCorreoNoEncontrado = stringResource(R.string.correo_no_encontrado)
    val textoCodigoRequerido = stringResource(R.string.codigo_es_requerido)
    val textoCodigoIncorrecto = stringResource(R.string.codigo_incorrecto)


    var boolOTP by remember { mutableStateOf(false) }
    var txtFieldCodigo by remember { mutableStateOf("") }

    // Animación y diseño
    val composition by rememberLottieComposition(LottieCompositionSpec.RawRes(R.raw.jsonemail))
    val progress by animateLottieCompositionAsState(
        composition = composition,
        iterations = LottieConstants.IterateForever
    )

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.Black)
            .imePadding() // Acomoda el padding inferior cuando aparece el teclado
    ) {
        Column(
            modifier = Modifier
                .verticalScroll(rememberScrollState())
                .fillMaxHeight()
        ) {
            LottieAnimation(
                composition = composition,
                progress = { progress },
                modifier = Modifier
                    .height(225.dp)
                    .align(Alignment.CenterHorizontally) // Centrado horizontalmente
                    .padding(top = 24.dp) // Espaciado superior si quieres
            )

            // Título
            Text(
                text = stringResource(id = R.string.recuperacion_de_contrasena),
                fontFamily = FontFamily(Font(R.font.arthura_medium)),
                color = Color.White,
                fontSize = 24.sp,
                modifier = Modifier
                    .offset(y = (-20).dp)
                    .fillMaxWidth(),
                textAlign = TextAlign.Center
            )

            if(boolOTP){
                // Card ingreso de codigo

                Card(
                    modifier = Modifier
                        .padding(horizontal = 12.dp, vertical = 24.dp) // Más espacio exterior
                        .fillMaxWidth(),
                    shape = RoundedCornerShape(25.dp),
                    elevation = CardDefaults.cardElevation(defaultElevation = 10.dp)
                ) {
                    Column(
                        modifier = Modifier
                            .background(Color.White)
                            .padding(vertical = 32.dp, horizontal = 16.dp), // Más espacio interior
                        horizontalAlignment = Alignment.CenterHorizontally // Centra todo horizontalmente
                    ) {
                        Text(
                            text = stringResource(id = R.string.ingresar_codigo),
                            fontFamily = FontFamily(Font(R.font.arthura_regular)),
                            color = Color.Black,
                            fontSize = 18.sp,
                            textAlign = TextAlign.Center
                        )

                        Spacer(modifier = Modifier.height(24.dp)) // Más espacio arriba del OTP

                        OtpTextField(
                            codigo = txtFieldCodigo,
                            onTextChanged = { newText ->
                                txtFieldCodigo = newText
                            }
                        )

                        Spacer(modifier = Modifier.height(10.dp))

                        Button(
                            onClick = {
                                // comprobar codigo

                                keyboardController?.hide()

                                when {
                                    txtFieldCodigo.isBlank() -> {
                                        CustomToasty(
                                            ctx,
                                            textoCodigoRequerido,
                                            ToastType.INFO
                                        )
                                    }

                                    txtFieldCodigo.length < 4 -> {
                                        CustomToasty(
                                            ctx,
                                            textoCodigoRequerido,
                                            ToastType.INFO
                                        )
                                    }
                                    else -> {
                                        // PASA VALIDACION
                                        // codigo correo

                                        viewModelVerificar.verificarCodigoRetrofit(txtFieldCodigo, correo)
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
                                text = stringResource(id = R.string.enviar_codigo),
                                fontSize = 18.sp,
                                style = TextStyle(
                                    fontSize = 20.sp,
                                    fontWeight = FontWeight.Medium,
                                )
                            )
                        }


                        Spacer(modifier = Modifier.height(32.dp)) // Más espacio debajo del OTP
                    }
                }

            }else{
                // Card ingreso de correo
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
                            text = stringResource(id = R.string.ingrese_su_correo_registrado),
                            fontFamily = FontFamily(Font(R.font.arthura_regular)),
                            color = Color.Black,
                            fontSize = 18.sp,
                            modifier = Modifier
                                .fillMaxWidth(),
                            textAlign = TextAlign.Center
                        )

                        Spacer(modifier = Modifier.height(6.dp))

                        // bloque para correo opciones
                        BloqueTextFieldCorreo(text = correo?: "",
                            textoPlaceholder = stringResource(R.string.correo_electronico),
                            onTextChanged = { newText -> viewModel.setCorreo(newText) },
                            maxLength = 100
                        )

                        Spacer(modifier = Modifier.height(6.dp))

                        Button(
                            onClick = {
                                // Comprobar ingreso de correo

                                keyboardController?.hide()

                                when {
                                    correo.isBlank() -> {
                                        CustomToasty(
                                            ctx,
                                            textoCorreoRequerido,
                                            ToastType.INFO
                                        )
                                    }

                                    !esCorreoValido(correo) -> {
                                        CustomToasty(
                                            ctx,
                                            textoCorreoInvalido,
                                            ToastType.INFO
                                        )
                                    }

                                    else -> {
                                        // PASA VALIDACION DE CORREO, SOLICITAR CODIGO
                                        viewModel.enviarCorreoRetrofit()
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
                                text = stringResource(id = R.string.enviar_codigo),
                                fontSize = 18.sp,
                                style = TextStyle(
                                    fontSize = 20.sp,
                                    fontWeight = FontWeight.Medium,
                                )
                            )
                        }

                        Spacer(modifier = Modifier.height(10.dp))
                    }
                } // end-card
            }

            if (isLoading) {
                LoadingModal(isLoading = isLoading)
            }

            if (isLoadingVerificar) {
                LoadingModal(isLoading = isLoadingVerificar)
            }

            resultado?.getContentIfNotHandled()?.let { result ->
                when (result.success) {

                    1 -> {
                        // CODIGO ENVIADO
                        CustomToasty(
                            ctx,
                            textoCodigoEnviado,
                            ToastType.INFO
                        )
                        boolOTP = true
                    }
                    2 -> {
                        // CORREO NO ENCONTRADO
                        CustomToasty(
                            ctx,
                            textoCorreoNoEncontrado,
                            ToastType.INFO
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

            resultadoVerificar?.getContentIfNotHandled()?.let { result ->
                when (result.success) {

                    1 -> {

                        // se obtiene id del usuario
                        val idusuario = result.id.toString()


                        // PASAR A PANTALLA CAMBIAR CONTRASENA
                        scope.launch {
                            navController.navigate(
                                Routes.VistaCambiarPasswordCorreo.createRoute(
                                    idusuario
                                ),
                                navOptions {
                                    launchSingleTop = true
                                }
                            )
                        }
                    }
                    2 -> {
                        // CODIGO INCORRECTO
                        CustomToasty(
                            ctx,
                            textoCodigoIncorrecto,
                            ToastType.INFO
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
        }
    }
}