<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<!DOCTYPE html>
<html lang="es">
<head>
    <meta charset="UTF-8">
    <title>Validar código - Ferretería Marvela</title>
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
            0% { background-position: 0% 50%; }
            100% { background-position: 100% 50%; }
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

        .btn-validar {
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
    <i class="fas fa-key position-absolute" style="top: 10%; left: 5%; font-size: 80px; color: rgba(0,0,0,0.05);"></i>
    <i class="fas fa-lock position-absolute" style="bottom: 10%; right: 5%; font-size: 80px; color: rgba(0,0,0,0.05);"></i>
    <i class="fas fa-shield-alt position-absolute" style="top: 50%; left: 10%; font-size: 70px; color: rgba(0,0,0,0.05);"></i>
    <i class="fas fa-user-check position-absolute" style="bottom: 20%; right: 15%; font-size: 60px; color: rgba(0,0,0,0.05);"></i>

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
                                <h4 class="mt-1 mb-4">Validar código de recuperación</h4>
                                <p class="mb-4">Ingresa el código que recibiste por correo para continuar.</p>
                            </div>

                            <form method="POST" action="UsuarioController">
                                <input type="hidden" name="consultar_datos" value="validarCodigo">

                                <div class="input-group mb-4">
                                    <span class="input-group-text bg-success text-white">
                                        <i class="fas fa-envelope"></i>
                                    </span>
                                    <input type="email" name="correo" class="form-control" placeholder="Correo electrónico" required>
                                </div>

                                <div class="input-group mb-4">
                                    <span class="input-group-text bg-success text-white">
                                        <i class="fas fa-key"></i>
                                    </span>
                                    <input type="text" name="codigo" class="form-control" placeholder="Código temporal" required>
                                </div>

                                <div class="text-center">
                                    <button class="btn btn-success btn-validar btn-block mb-2" type="submit">
                                        <i class="fas fa-check-circle me-2"></i>Validar código
                                    </button>
                                </div>

                                <div class="text-center">
                                    <a href="recuperar_contraseña.jsp" class="text-volver">← Volver a recuperar contraseña</a>
                                </div>
                            </form>

                            <c:if test="${not empty error}">
                                <div class="text-center mt-3">
                                    <p style="color:red">${error}</p>
                                </div>
                            </c:if>
                        </div>
                    </div>
                </div>
            </div>
        </div>
    </section>

    <!-- Scripts -->
    <script src="https://code.jquery.com/jquery-3.6.0.min.js"></script>
    <script src="https://cdnjs.cloudflare.com/ajax/libs/mdb-ui-kit/6.4.2/mdb.min.js"></script>
</body>
</html>