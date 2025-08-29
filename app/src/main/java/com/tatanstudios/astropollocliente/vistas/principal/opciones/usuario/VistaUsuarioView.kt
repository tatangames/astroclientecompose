package com.tatanstudios.astropollocliente.vistas.principal.opciones.usuario

import androidx.compose.animation.core.animateDpAsState
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
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
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
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
import com.tatanstudios.astropollocliente.componentes.BarraToolbarColor
import com.tatanstudios.astropollocliente.componentes.BloqueTextFieldCorreoUsuario
import com.tatanstudios.astropollocliente.componentes.BloqueTextFieldUsuario
import com.tatanstudios.astropollocliente.componentes.CustomModal1Boton
import com.tatanstudios.astropollocliente.componentes.CustomToasty
import com.tatanstudios.astropollocliente.componentes.LoadingModal
import com.tatanstudios.astropollocliente.componentes.ToastType
import com.tatanstudios.astropollocliente.extras.TokenManager
import com.tatanstudios.astropollocliente.viewmodel.ActualizarCorreoUsuarioViewModel
import com.tatanstudios.astropollocliente.viewmodel.InformacionUsuarioViewModel
import com.tatanstudios.astropollocliente.vistas.login.esCorreoValido
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch

@Composable
fun MiUsuarioScreen(navController: NavHostController,
                   viewModelInformacion: InformacionUsuarioViewModel = viewModel(),
                   viewModelActualizar: ActualizarCorreoUsuarioViewModel = viewModel(),
) {

    val ctx = LocalContext.current

    val resultadoInformacion by viewModelInformacion.resultado.observeAsState()
    val isLoadingInformacion by viewModelInformacion.isLoading.observeAsState(false)

    val resultadoActualizar by viewModelActualizar.resultado.observeAsState()
    val isLoadingActualizar by viewModelActualizar.isLoading.observeAsState(false)

    var idusuario by remember { mutableStateOf("") }

    val interactionSource = remember { MutableInteractionSource() }
    val isPressed by interactionSource.collectIsPressedAsState()
    val keyboardController = LocalSoftwareKeyboardController.current

    val tokenManager = remember { TokenManager(ctx) }

    var _usuario by remember { mutableStateOf("") }
    var _correo by remember { mutableStateOf("") }

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


    val scrollState = rememberScrollState()


    LaunchedEffect(Unit) {
        scope.launch {
            idusuario = tokenManager.idUsuario.first()
            viewModelInformacion.informacionUsuarioRetrofit(idusuario)
        }
    }

    Scaffold(
        topBar = {
            BarraToolbarColor(
                navController,
                stringResource(R.string.menu),
                colorResource(R.color.colorRojo)
            )
        }
    ) { innerPadding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .imePadding()
                .padding(innerPadding)
        ) {
            Column(
                modifier = Modifier
                    .verticalScroll(scrollState)
                    .fillMaxHeight()
            ) {
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


                        Spacer(modifier = Modifier.height(12.dp))


                        BloqueTextFieldUsuario(
                            text = _usuario,
                            onTextChanged = { newText -> _usuario = newText },
                            maxLength = 20,
                            labelText = stringResource(R.string.usuario)
                        )

                        Spacer(modifier = Modifier.height(6.dp))

                        // bloque para correo opciones
                        BloqueTextFieldCorreoUsuario(
                            text = _correo,
                            textoPlaceholder = stringResource(R.string.correo_electronico),
                            onTextChanged = { _correo = it },
                            maxLength = 100,
                            labelText = stringResource(R.string.correo_electronico)
                        )

                        Spacer(modifier = Modifier.height(6.dp))

                        Button(
                            onClick = {
                                keyboardController?.hide()

                                when {
                                    _usuario.isBlank() -> {
                                        modalMensajeString = ctx.getString(R.string.usuario_es_requerido)
                                        showModal1Boton = true
                                    }

                                    _correo.isBlank() -> {
                                        modalMensajeString = ctx.getString(R.string.correo_es_requerido)
                                        showModal1Boton = true
                                    }

                                    !_correo.isNullOrBlank() && !esCorreoValido(_correo!!) -> {
                                        modalMensajeString = ctx.getString(R.string.correo_ingresado_no_es_valido)
                                        showModal1Boton = true
                                    }

                                    else -> {
                                        scope.launch {
                                            viewModelActualizar.actualizarUsuarioRetrofit(
                                                idusuario,
                                                _usuario,
                                                _correo
                                            )
                                        }
                                    }
                                }
                            },
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(top = 32.dp, start = 24.dp, end = 24.dp)
                                .shadow(
                                    elevation = elevation,
                                    shape = RoundedCornerShape(25.dp)
                                ),
                            colors = ButtonDefaults.buttonColors(
                                containerColor = loginButtonColor,
                                contentColor = colorResource(R.color.colorBlanco),
                            ),
                            interactionSource = interactionSource
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
                }
            }

            // Modales y Loadings fuera de la Card

            if(showModal1Boton){
                CustomModal1Boton(showModal1Boton, modalMensajeString, onDismiss = {showModal1Boton = false})
            }

            if (isLoadingInformacion) {
                LoadingModal(isLoading = isLoadingInformacion)
            }

            if (isLoadingActualizar) {
                LoadingModal(isLoading = isLoadingActualizar)
            }

            resultadoInformacion?.getContentIfNotHandled()?.let { result ->
                when (result.success) {
                    1 -> {
                        _usuario = result.usuario ?: ""
                        _correo = result.correo ?: ""
                    }

                    else -> {
                        CustomToasty(
                            ctx,
                            stringResource(id = R.string.error_reintentar_de_nuevo),
                            ToastType.ERROR
                        )
                    }
                }
            }


            resultadoActualizar?.getContentIfNotHandled()?.let { result ->
                when (result.success) {
                    1 -> {
                        // USUARIO ESTA REPETIDO
                        modalMensajeString = stringResource(R.string.otro_usuario_ya_tiene)
                        showModal1Boton = true
                    }
                    2 -> {
                        // CORREO ESTA REPETIDO
                        modalMensajeString = stringResource(R.string.otro_correo_ya_tiene)
                        showModal1Boton = true
                    }
                    3 -> {
                        // ACTUALIZADO

                        CustomToasty(
                            ctx,
                            stringResource(id = R.string.actualizado),
                            ToastType.SUCCESS
                        )
                    }
                    else -> {
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