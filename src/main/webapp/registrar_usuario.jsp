<%-- 
    Document   : registrar_usuario.jsp
    Created on : 28 oct. 2025
    Author     : Maris
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<!DOCTYPE html>
<html lang="es">
    <head>
        <meta charset="UTF-8">
        <title>Registrar Usuario - Ferretería Marvela</title>
        <link href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.0.0/css/all.min.css" rel="stylesheet"/>
        <link href="https://fonts.googleapis.com/css?family=Roboto:300,400,500,700&display=swap" rel="stylesheet"/>
        <link href="https://cdnjs.cloudflare.com/ajax/libs/mdb-ui-kit/6.4.2/mdb.min.css" rel="stylesheet"/>

        <style>
            body {
                background: linear-gradient(135deg, #c8e6c9, #a5d6a7, #81c784);
                background-size: 600% 600%;
                animation: fondoMovimiento 20s ease infinite;
                font-family: 'Roboto', sans-serif;
            }

            @keyframes fondoMovimiento {
                0% {
                    background-position: 0% 50%;
                }
                100% {
                    background-position: 100% 50%;
                }
            }

            .gradient-form {
                height: 100vh;
            }

            .logo-box {
                background-color: #ffffff;
                border-radius: 50%;
                padding: 10px;
                box-shadow: 0 0 10px rgba(0,0,0,0.1);
                margin-bottom: 10px;
            }

            .btn-registrar {
                font-size: 14px;
                padding: 8px 16px;
            }

            .input-group-text {
                background-color: #388e3c;
                color: #fff;
                border: none;
            }

            .text-volver {
                font-size: 13px;
                color: #388e3c;
            }

            .text-volver:hover {
                text-decoration: underline;
                color: #2e7d32;
            }
        </style>
    </head>
    <body>

        <!-- Íconos flotantes -->
        <i class="fas fa-user position-absolute" style="top: 10%; left: 5%; font-size: 80px; color: rgba(0,0,0,0.05);"></i>
        <i class="fas fa-envelope position-absolute" style="bottom: 10%; right: 5%; font-size: 80px; color: rgba(0,0,0,0.05);"></i>
        <i class="fas fa-lock position-absolute" style="top: 50%; left: 10%; font-size: 70px; color: rgba(0,0,0,0.05);"></i>
        <i class="fas fa-user-shield position-absolute" style="bottom: 20%; right: 15%; font-size: 60px; color: rgba(0,0,0,0.05);"></i>

        <section class="gradient-form">
            <div class="container py-5 h-100">
                <div class="row d-flex justify-content-center align-items-center h-100">
                    <div class="col-xl-6">
                        <div class="card rounded-3 text-black shadow">
                            <div class="card-body p-md-5 mx-md-4">
                                <div class="text-center">
                                    <div class="logo-box">
                                        <img src="img/Login2.png" style="width: 80px;" alt="Logo Ferretería">
                                    </div>
                                    <h4 class="mt-1 mb-4">Registro de Usuario</h4>
                                    <p class="mb-4">Crea el usuario vinculado al empleado registrado.</p>

                                    <!-- Depuración temporal -->

                                </div>

                                <form method="POST" action="UsuarioController">
                                    <input type="hidden" name="formulario_enviado" value="true">
                                    <input type="hidden" name="consultar_datos" value="registrarUsuario">
                                    <input type="hidden" name="codigo_empleado" value="${codigo_empleado}">
                                    <input type="hidden" name="nombre_empleado" value="${nombre_empleado}">

                                    <div class="input-group mb-4">
                                        <span class="input-group-text">
                                            <i class="fas fa-envelope"></i>
                                        </span>
                                        <input type="email" name="correo" class="form-control" placeholder="Correo electrónico" required>
                                    </div>

                                    <div class="input-group mb-4">
                                        <span class="input-group-text">
                                            <i class="fas fa-lock"></i>
                                        </span>
                                        <input type="password" name="clave" class="form-control" placeholder="Contraseña segura" required>
                                    </div>

                                    <div class="text-center">
                                        <button class="btn btn-success btn-registrar btn-block mb-2" type="submit">
                                            <i class="fas fa-user-plus me-2"></i>Registrar usuario
                                        </button>
                                    </div>
                                </form>

                             <c:if test="${not empty mensaje}">
    <div id="mensajeExito" class="alert alert-success text-center mt-3 fade show" role="alert" style="font-size:16px; transition: opacity 0.5s ease;">
        <i class="fas fa-check-circle me-2"></i>${mensaje}
    </div>

    <script>
        setTimeout(function () {
            const mensaje = document.getElementById("mensajeExito");
            if (mensaje) {
                mensaje.classList.remove("show");
                mensaje.classList.add("fade");
                mensaje.style.opacity = "0";
            }
        }, 3000); // 3000 milisegundos = 3 segundos
    </script>
</c:if>


                                <!-- ✅ Mensaje de error dentro del formulario -->
                                <c:if test="${not empty error}">
                                    <div class="alert alert-danger text-center mt-3" role="alert" style="font-size:16px;">
                                        <i class="fas fa-exclamation-circle me-2"></i>${error}
                                    </div>
                                </c:if>

                            </div>
                        </div>
                    </div>
                </div>
            </div>
        </section>
<script>
document.getElementById("clave").addEventListener("input", function () {
    const clave = this.value;
    const mensaje = document.getElementById("mensajeClave");

    const tieneEspecial = /[!@#$%^&*()_+=\-{}\[\]:;"'<>,.?/\\|]/.test(clave);
    const longitudValida = clave.length >= 8 && clave.length <= 12;

    if (!longitudValida || !tieneEspecial) {
        mensaje.style.display = "block";
        mensaje.textContent = "La contraseña debe tener entre 8 y 12 caracteres y al menos un carácter especial.";
    } else {
        mensaje.style.display = "none";
    }
});
</script>
        <!-- Scripts -->
        <script src="https://code.jquery.com/jquery-3.6.0.min.js"></script>
        <script src="https://cdnjs.cloudflare.com/ajax/libs/mdb-ui-kit/6.4.2/mdb.min.js"></script>
    </body>
</html>