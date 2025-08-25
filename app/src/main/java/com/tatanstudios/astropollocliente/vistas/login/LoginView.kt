package com.tatanstudios.astropollocliente.vistas.login

import androidx.compose.animation.core.animateDpAsState
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
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
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.lifecycle.viewmodel.compose.viewModel
import com.onesignal.OneSignal
import com.tatanstudios.astropollocliente.componentes.BloqueTextFieldLogin
import com.tatanstudios.astropollocliente.componentes.BloqueTextFieldPassword
import com.tatanstudios.astropollocliente.componentes.CustomModal1Boton
import com.tatanstudios.astropollocliente.componentes.CustomToasty
import com.tatanstudios.astropollocliente.componentes.LoadingModal
import com.tatanstudios.astropollocliente.componentes.ToastType
import com.tatanstudios.astropollocliente.extras.TokenManager
import com.tatanstudios.astropollocliente.model.rutas.Routes
import com.tatanstudios.astropollocliente.viewmodel.LoginViewModel
import kotlinx.coroutines.launch

@Composable
fun LoginScreen(navController: NavHostController, viewModel: LoginViewModel = viewModel()) {

    val ctx = LocalContext.current
    val usuario by viewModel.usuario.observeAsState("")
    val password by viewModel.password.observeAsState("")
    val resultado by viewModel.resultado.observeAsState()
    val isLoading by viewModel.isLoading.observeAsState(false)

    var isPasswordVisible by remember { mutableStateOf(false) } // Control de visibilidad de la contraseña
    val interactionSource = remember { MutableInteractionSource() }
    val isPressed by interactionSource.collectIsPressedAsState()
    val keyboardController = LocalSoftwareKeyboardController.current

    val tokenManager = remember { TokenManager(ctx) }

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

    var idonesignal by remember { mutableStateOf("") }

    LaunchedEffect(Unit) {
        scope.launch {
            idonesignal = getOneSignalUserId()
        }
    }

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


            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(150.dp)
                    .background(Color.White)
            ) {
                // Logo centrado en el fondo blanco

                Image(
                    painter = painterResource(id = R.drawable.logonegrocirculo),
                    contentDescription = stringResource(id = R.string.logotipo),
                    modifier = Modifier
                        .size(130.dp)
                        .align(Alignment.Center)
                        .padding(top = 24.dp)
                )
            }


            // Imagen tipo onda (wave) justo debajo del fondo blanco
            Image(
                painter = painterResource(id = R.drawable.wave_onda),
                contentDescription = null,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(90.dp),
                  //  .offset(y = (-18).dp),
                contentScale = ContentScale.Crop // Esto estira la imagen horizontalmente
            )

            // Título
            Text(
                text = stringResource(id = R.string.astro_pollo),
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

                    Text(
                        text = stringResource(R.string.contrasena_olvidada),
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(end = 24.dp, top = 4.dp)
                            .clickable {
                                navController.navigate(Routes.VistaRecuperarCorreo.route) {
                                    popUpTo(Routes.VistaRecuperarCorreo.route) { inclusive = true }
                                }
                            },
                        textAlign = TextAlign.End,
                        color = Color.Gray,
                        fontSize = 14.sp,
                        fontWeight = FontWeight.Medium
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
                                else -> {
                                    viewModel.verificarUsuarioPasssword(idonesignal = idonesignal)
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
                            text = stringResource(id = R.string.iniciar_sesion),
                            fontSize = 18.sp,
                            style = TextStyle(
                                fontSize = 20.sp,
                                fontWeight = FontWeight.Medium,
                            )
                        )
                    }

                    Spacer(modifier = Modifier.height(26.dp))

                    Text(
                        text = stringResource(R.string.registrarse),
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(top = 16.dp)
                            .clickable {
                                navController.navigate(Routes.VistaRegistro.route) {
                                    popUpTo(Routes.VistaRegistro.route) { inclusive = true }
                                }
                            },
                        textAlign = TextAlign.Center,
                        color = Color.Gray,
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Bold
                    )

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
                            // USUARIO BLOQUEADO
                            modalMensajeString = ctx.getString(R.string.usuario_bloqueado)
                            showModal1Boton = true
                        }
                        2 -> {

                            // INICIO SESION CORRECTO

                            val _id = (result.id).toString()

                            scope.launch {
                                tokenManager.saveID(_id)

                                navController.navigate(Routes.VistaPrincipal.route) {
                                    popUpTo(0) { // Esto elimina todas las vistas de la pila de navegación
                                        inclusive = true // Asegura que ninguna pantalla anterior quede en la pila
                                    }
                                    launchSingleTop = true // Evita múltiples instancias de la misma vista
                                }
                            }
                        }
                        3 -> {
                            // CONTRASENA INCORRECTA
                            CustomToasty(
                                ctx,
                                stringResource(id = R.string.password_incorrecto),
                                ToastType.INFO
                            )
                        }
                        4 -> {
                            // USUARIO INCORRECTO
                            CustomToasty(
                                ctx,
                                stringResource(id = R.string.usuario_incorrecto),
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

        }
    }


}


// ID DE ONE SIGNAL
fun getOneSignalUserId(): String {
    val deviceState = OneSignal.User.pushSubscription.id
    return deviceState
}


/*@Preview(showBackground = true)
@Composable
fun PreviewLoginScreen() {
    val navController = rememberNavController()
    LoginScreen(navController = navController)
}*/