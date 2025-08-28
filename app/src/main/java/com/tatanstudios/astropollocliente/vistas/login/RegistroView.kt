package com.tatanstudios.astropollocliente.vistas.login

import android.content.Context
import android.content.pm.PackageManager
import android.util.Patterns
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
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
import androidx.compose.ui.res.painterResource
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
import androidx.compose.foundation.layout.Row
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
import com.airbnb.lottie.compose.LottieAnimation
import com.airbnb.lottie.compose.LottieCompositionSpec
import com.airbnb.lottie.compose.LottieConstants
import com.airbnb.lottie.compose.animateLottieCompositionAsState
import com.airbnb.lottie.compose.rememberLottieComposition
import com.tatanstudios.astropollocliente.componentes.BloqueTextFieldCorreo
import com.tatanstudios.astropollocliente.componentes.BloqueTextFieldLogin
import com.tatanstudios.astropollocliente.componentes.BloqueTextFieldPassword
import com.tatanstudios.astropollocliente.componentes.CustomModal1Boton
import com.tatanstudios.astropollocliente.componentes.CustomModal1BotonTitulo
import com.tatanstudios.astropollocliente.componentes.CustomModal2Botones
import com.tatanstudios.astropollocliente.componentes.CustomToasty
import com.tatanstudios.astropollocliente.componentes.LoadingModal
import com.tatanstudios.astropollocliente.componentes.ToastType
import com.tatanstudios.astropollocliente.extras.TokenManager
import com.tatanstudios.astropollocliente.model.rutas.Routes
import com.tatanstudios.astropollocliente.viewmodel.RegistroViewModel
import kotlinx.coroutines.launch

@Composable
fun RegistroScreen(navController: NavHostController, viewModel: RegistroViewModel = viewModel()) {

    val ctx = LocalContext.current
    val usuario by viewModel.usuario.observeAsState("")
    val password by viewModel.password.observeAsState("")
    val correo by viewModel.correo.observeAsState("")


    val resultado by viewModel.resultado.observeAsState()
    val isLoading by viewModel.isLoading.observeAsState(false)

    val interactionSource = remember { MutableInteractionSource() }
    val isPressed by interactionSource.collectIsPressedAsState()
    val keyboardController = LocalSoftwareKeyboardController.current

    val tokenManager = remember { TokenManager(ctx) }
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

    // titulo y mensaje de respuestas
    var textoTituloApi by remember { mutableStateOf("") }
    var textoMensajeApi by remember { mutableStateOf("") }
    var showDialogApi by remember { mutableStateOf(false) }

    var showModal2Boton by remember { mutableStateOf(false) }
    var idonesignal by remember { mutableStateOf("") }

    LaunchedEffect(Unit) {
        scope.launch {
            idonesignal = getOneSignalUserId()
        }
    }

    val texto4CaracteresMinimo = stringResource(R.string.minimo_4_caracteres)

    // Animación y diseño
    val composition by rememberLottieComposition(LottieCompositionSpec.RawRes(R.raw.jsonhotdog))
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
                text = stringResource(id = R.string.crear_una_cuenta),
                fontFamily = FontFamily(Font(R.font.arthura_medium)),
                color = Color.White,
                fontSize = 30.sp,
                modifier = Modifier
                    .offset(y = (-20).dp)
                    .fillMaxWidth(),
                textAlign = TextAlign.Center
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

                    BloqueTextFieldLogin(text = usuario,
                        onTextChanged = { newText -> viewModel.setUsuario(newText) },
                        maxLength = 20
                    )

                    // Bloque para la contraseña
                    BloqueTextFieldPassword(
                        text = password,
                        onTextChanged = { newText -> viewModel.setPassword(newText) },
                        isPasswordVisible = isPasswordVisible,
                        onPasswordVisibilityChanged = { isPasswordVisible = it },
                        maxLength = 16
                    )

                    // bloque para correo opciones
                    BloqueTextFieldCorreo(text = correo?: "", textoPlaceholder = stringResource(R.string.correo_opcional),
                        onTextChanged = { newText -> viewModel.setCorreo(newText) },
                        maxLength = 100
                    )


                    Spacer(modifier = Modifier.height(6.dp))

                    Button(
                        onClick = {
                            // Acción de login

                            keyboardController?.hide()

                            when {
                                usuario.isBlank() -> {
                                    modalMensajeString = ctx.getString(R.string.usuario_es_requerido)
                                    showModal1Boton = true
                                }

                                password.isBlank() -> {
                                    modalMensajeString = ctx.getString(R.string.password_es_requerido)
                                    showModal1Boton = true
                                }

                                password.length < 4 -> {
                                    modalMensajeString = texto4CaracteresMinimo
                                    showModal1Boton = true
                                }


                                !correo.isNullOrBlank() && !esCorreoValido(correo!!) -> {
                                    modalMensajeString = ctx.getString(R.string.correo_ingresado_no_es_valido)
                                    showModal1Boton = true
                                }

                                else -> {
                                    showModal2Boton = true
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
                            text = stringResource(id = R.string.registrarse),
                            fontSize = 18.sp,
                            style = TextStyle(
                                fontSize = 20.sp,
                                fontWeight = FontWeight.Medium,
                            )
                        )
                    }

                    Spacer(modifier = Modifier.height(10.dp))
                }

                if(showDialogApi){
                    CustomModal1BotonTitulo(
                        showDialog = showDialogApi,
                        title = textoTituloApi,
                        message = textoMensajeApi,
                        onDismiss = {
                            showDialogApi = false

                        }
                    )
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
                            // USUARIO YA REGISTRADO
                            textoTituloApi = result.titulo?: ""
                            textoMensajeApi = result.mensaje?: ""
                            showDialogApi = true
                        }
                        2 -> {

                            // CORREO YA REGISTRADO
                            textoTituloApi = result.titulo?: ""
                            textoMensajeApi = result.mensaje?: ""
                            showDialogApi = true
                        }
                        3 -> {
                            // REGISTRADO CORRECTAMENTE

                            val _id = (result.id).toString()

                            scope.launch {
                                tokenManager.saveID(_id)

                                /*navController.navigate(Routes.VistaPrincipal.route) {
                                    popUpTo(0) { // Esto elimina todas las vistas de la pila de navegación
                                        inclusive = true // Asegura que ninguna pantalla anterior quede en la pila
                                    }
                                    launchSingleTop = true // Evita múltiples instancias de la misma vista
                                }*/

                                navController.navigate(
                                    Routes.VistaPrincipal.createRoute("menu")
                                ) {
                                    popUpTo(0) { // Esto elimina todas las vistas de la pila de navegación
                                        inclusive = true // Asegura que ninguna pantalla anterior quede en la pila
                                    }
                                    launchSingleTop = true // Evita múltiples instancias de la misma vista
                                }
                            }
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


            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Spacer(modifier = Modifier.weight(1f)) // Empuja la imagen hacia la derecha

                Image(
                    painter = painterResource(id = R.drawable.cubetapollo2),
                    contentDescription = stringResource(id = R.string.logotipo),
                    modifier = Modifier
                        .size(width = 120.dp, height = 100.dp)
                )
            }

            // CONFIRMAR PARA REGISTRARSE
            if(showModal2Boton){
                CustomModal2Botones(
                    showDialog = true,
                    message = stringResource(R.string.registrarse),
                    onDismiss = { showModal2Boton = false },
                    onAccept = {
                        showModal2Boton = false

                        scope.launch {
                            viewModel.registroRetrofit(
                                idonesignal,
                                version = getVersionName(ctx)
                            )
                        }

                    },
                    stringResource(R.string.si),
                    stringResource(R.string.no),
                )
            }

        }
    }
}

fun esCorreoValido(correo: String): Boolean {
    return Patterns.EMAIL_ADDRESS.matcher(correo).matches()
}



fun getVersionName(context: Context): String {
    return try {
        val packageInfo = context.packageManager.getPackageInfo(context.packageName, 0)
        packageInfo.versionName ?: "N/A"
    } catch (e: PackageManager.NameNotFoundException) {
        "N/A"
    }
}