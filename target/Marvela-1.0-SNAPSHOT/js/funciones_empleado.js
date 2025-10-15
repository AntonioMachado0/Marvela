$(function () {
// Establecer fecha por defecto inmediatamente al cargar la página
establecerFechaPorDefecto();
        cargarTabla();
        // Eventos para el modal de registro
        $('#md_registrar').on('show.bs.modal', function () {
establecerFechaPorDefecto();
        $('#numero_telefono').val(''); // Limpiar teléfono al abrir modal
});
        $('#md_registrar').on('shown.bs.modal', function () {
$('#numero_telefono').on('input', formatearTelefono);
});
        // Eventos para el modal de edición
        $('#myModalE').on('shown.bs.modal', function () {
$('#numero_telefonoE').on('input', formatearTelefono);
});
        $(document).on("submit", "#formulario_registro", function (e) {
e.preventDefault();
        // ✅ VALIDAR NOMBRE PRIMERO (ASÍ SE PONDRÁ ROJO SI HAY ERROR)
        const nombreInput = document.getElementById('nombre_completo');
        if (!validarNombreSubmit(nombreInput)) {
// Hacer scroll al campo con error
nombreInput.scrollIntoView({behavior: 'smooth', block: 'center'});
        nombreInput.focus();
        return false;
}
// Validar edad
const fechaNacimiento = $("#fecha_nacimiento").val();
        if (!validarEdad(fechaNacimiento)) {
Swal.fire("Error", "El empleado debe tener entre 18 y 65 años", "error");
        return false;
}

// Validar formato de teléfono
const telefono = $("#numero_telefono").val();
        if (!validarFormatoTelefono(telefono)) {
Swal.fire("Error", "El formato del teléfono debe ser ####-####", "error");
        return false;
}

var datos = $("#formulario_registro").serialize();
        $.ajax({
        dataType: "json",
                method: "POST",
                url: "EmpleadoServlet",
                data: datos + "&consultar_datos=si_registro"
        }).done(function (json) {
if (json[0].resultado === "exito") {
$("#formulario_registro")[0].reset();
        $("#md_registrar").modal("hide");
        cargarTabla();
        Swal.fire("Excelente", "El empleado fue agregado", "success");
} else {
Swal.fire("Acción no completada", json[0].mensaje || "No se puede registrar un nuevo empleado", "error");
}
}).fail(function (xhr, status, error) {
console.error("Error AJAX:", status, error);
        Swal.fire("Error", "Problema de conexión", "error");
});
});
        // ✅ Botón para abrir modal con datos cargados
        $(document).on("click", ".btn_editar", function (e) {
e.preventDefault();
        var idEmpleado = $(this).attr("data-id");
        cargarDatosModalEmpleado(idEmpleado);
});
        // VALIDACIÓN EN TIEMPO REAL PARA AMBOS CAMPOS
        $(document).ready(function() {
$('#nombre_completo').on('input', function() {
validarNombre(this);
});
        $('#nombre_completoE').on('input', function() {
validarNombre(this);
});
});
        function cargarDatosModalEmpleado(codigo_empleado) {
        $.ajax({
        dataType: "json",
                method: "POST",
                url: "EmpleadoServlet",
                data: {"codigo_empleado": codigo_empleado, "consultar_datos": "obtener_uno"}
        }).done(function (json) {
        if (json.length > 0 && json[0].resultado === "exito") {
        var emp = json[0];
                $("#codigo_empleadoE").val(emp.codigo_empleadoE);
                $("#nombre_completoE").val(emp.nombre_completoE);
                $("#numero_telefonoE").val(emp.numero_telefonoE);
                $("#fecha_nacimientoE").val(emp.fecha_nacimientoE);
                $("#codigo_rolesE").val(emp.codigo_rolesE);
                $("#myModalE").modal("show");
        } else {
        Swal.fire("Error", json[0]?.mensaje || "No se pudo obtener los datos del empleado", "error");
        }
        }).fail(function () {
        Swal.fire("Error", "Problema en la petición AJAX", "error");
        });
        }

// ✅ Enviar actualización al servlet
$(document).on("submit", "#formulario_editar", function (e) {
e.preventDefault();
        const nombreInput = document.getElementById('nombre_completoE');
        if (!validarNombreSubmit(nombreInput)) {
nombreInput.scrollIntoView({behavior: 'smooth', block: 'center'});
        nombreInput.focus();
        return false;
}
// Validar edad en edición también
const fechaNacimiento = $("#fecha_nacimientoE").val();
        if (!validarEdad(fechaNacimiento)) {
Swal.fire("Error", "El empleado debe tener entre 18 y 65 años", "error");
        return false;
}

// Validar formato de teléfono en edición
const telefono = $("#numero_telefonoE").val();
        if (!validarFormatoTelefono(telefono)) {
Swal.fire("Error", "El formato del teléfono debe ser ####-####", "error");
        return false;
}

var datos = {
"codigo_empleadoE": $("#codigo_empleadoE").val(),
        "nombre_completoE": $("#nombre_completoE").val(),
        "numero_telefonoE": $("#numero_telefonoE").val(),
        "fecha_nacimientoE": $("#fecha_nacimientoE").val(),
        "codigo_rolesE": $("#codigo_rolesE").val(),
        "consultar_datos": "si_actualizalo"
};
        $.ajax({
        dataType: "json",
                method: "POST",
                url: "EmpleadoServlet",
                data: datos
        }).done(function (json) {
if (json[0].resultado === "exito") {
$("#formulario_editar")[0].reset();
        $("#myModalE").modal("hide");
        cargarTabla();
        Swal.fire("Excelente", "El empleado fue actualizado correctamente", "success");
} else {
Swal.fire("Acción no completada", json[0].mensaje || "No se pudo actualizar", "error");
}
}).fail(function () {
Swal.fire("Error", "Problema en la petición AJAX", "error");
});
});
        // METODO PARA CARGAR LA TABLA
                function cargarTabla(estado = 1) {
                var datos = {"consultar_datos": "si_consulta", "estado": estado};
                        $.ajax({
                        dataType: "json",
                                method: "POST",
                                url: "EmpleadoServlet",
                                data: datos
                        }).done(function (json) {
                if (json[0].resultado == "exito") {
                $("#empleadoA").empty().html(json[0].tabla);
                        // Destruir DataTable existente antes de inicializar uno nuevo
                        if ($.fn.DataTable.isDataTable('#tabla_empleados')) {
                $('#tabla_empleados').DataTable().destroy();
                }

                $('#tabla_empleados').DataTable({
                "language": {
                "url": "https://cdn.datatables.net/plug-ins/1.12.1/i18n/es-ES.json"
                },
                        lengthMenu: [[5, 10, 15, - 1], [5, 10, 15, "Todos"]],
                        pageLength: 5,
                        columnDefs: [
                        {responsivePriority: 1, targets: 0},
                        {responsivePriority: 2, targets: - 1}
                        ]
                });
                } else {
                Swal.fire("Acción no completada", json[0].mensaje || "No pudo obtener los datos", "error");
                }
                }).fail(function () {
                Swal.fire("Error", "Problema al cargar la tabla", "error");
                });
                }

        $(document).on("click", "#btn_cerrar", function (e) {
        e.preventDefault();
                $("#md_registrar").modal("hide");
        });
                function mostrar_cargando(titulo, mensaje = "") {
                Swal.fire({
                title: titulo,
                        html: mensaje,
                        timer: 2000,
                        timerProgressBar: true,
                        didOpen: () => {
                Swal.showLoading();
                }
                }).then((result) => {
                if (result.dismiss === Swal.DismissReason.timer) {
                console.log("I was closed by timer");
                }
                });
                }

        // FUNCIONES AUXILIARES NUEVAS

        function establecerFechaPorDefecto() {
        try {
        const today = new Date();
                const defaultDate = new Date(today.getFullYear() - 18, today.getMonth(), today.getDate());
                // Formatear a YYYY-MM-DD para input type="date"
                const year = defaultDate.getFullYear();
                const month = String(defaultDate.getMonth() + 1).padStart(2, '0');
                const day = String(defaultDate.getDate()).padStart(2, '0');
                const formattedDate = `${year}-${month}-${day}`;
                // Establecer el valor en el campo de fecha
                const fechaInput = document.getElementById('fecha_nacimiento');
                if (fechaInput) {
        fechaInput.value = formattedDate;
                console.log("Fecha establecida:", formattedDate);
        }
        } catch (error) {
        console.error("Error al establecer fecha por defecto:", error);
        }
        }

        function formatearTelefono(e) {
        const input = e.target;
                let value = input.value.replace(/\D/g, '');
                // Limitar a 8 dígitos
                if (value.length > 8) {
        value = value.substring(0, 8);
        }

        // Aplicar formato ####-####
        if (value.length <= 4) {
        input.value = value;
        } else {
        input.value = value.substring(0, 4) + '-' + value.substring(4, 8);
        }
        }

        function validarEdad(fechaNacimiento) {
        if (!fechaNacimiento)
                return false;
                const hoy = new Date();
                const nacimiento = new Date(fechaNacimiento);
                let edad = hoy.getFullYear() - nacimiento.getFullYear();
                const mes = hoy.getMonth() - nacimiento.getMonth();
                if (mes < 0 || (mes === 0 && hoy.getDate() < nacimiento.getDate())) {
        edad--;
        }

        return edad >= 18 && edad <= 100;
        }

        function validarFormatoTelefono(telefono) {
        return /^\d{4}-\d{4}$/.test(telefono);
        }


        // FUNCIÓN MEJORADA PARA VALIDACIÓN EN TIEMPO REAL
        function validarNombre(input) {
        const errorElement = document.getElementById(`error-${input.id}`);
                const regex = /^[a-zA-ZáéíóúÁÉÍÓÚñÑ\s]*$/; // Solo letras y espacios

                // Remover espacios en blanco al inicio y final
                const valor = input.value.trim();
                if (valor === "") {
        // Campo vacío - quitar estilos de error
        input.classList.remove('is-invalid', 'is-valid');
                input.style.borderColor = '';
                if (errorElement) errorElement.style.display = 'none';
                return true;
        }

        if (!regex.test(valor)) {
        // ❌ Caracteres inválidos - poner en ROJO inmediatamente
        input.classList.add('is-invalid');
                input.classList.remove('is-valid');
                input.style.borderColor = '#dc3545';
                input.style.boxShadow = '0 0 0 0.2rem rgba(220, 53, 69, 0.25)';
                if (errorElement) {
        errorElement.textContent = '❌ Solo se permiten letras y espacios';
                errorElement.style.display = 'block';
        }
        return false;
        } else {
        // ✅ Válido - poner en VERDE
        input.classList.remove('is-invalid');
                input.classList.add('is-valid');
                input.style.borderColor = '#28a745';
                input.style.boxShadow = '0 0 0 0.2rem rgba(40, 167, 69, 0.25)';
                if (errorElement) errorElement.style.display = 'none';
                return true;
        }
        }

        // FUNCIÓN PARA VALIDAR AL ENVIAR EL FORMULARIO
        function validarNombreSubmit(input) {
        const errorElement = document.getElementById(`error-${input.id}`);
                const regex = /^[a-zA-ZáéíóúÁÉÍÓÚñÑ\s]+$/; // Solo letras y espacios (al menos 1)

                if (input.value.trim() === "") {
        input.classList.add('is-invalid');
                input.style.borderColor = '#dc3545';
                if (errorElement) {
        errorElement.textContent = '❌ Este campo es obligatorio';
                errorElement.style.display = 'block';
        }
        return false;
        }

        if (!regex.test(input.value.trim())) {
        input.classList.add('is-invalid');
                input.style.borderColor = '#dc3545';
                if (errorElement) {
        errorElement.textContent = '❌ Solo se permiten letras y espacios';
                errorElement.style.display = 'block';
        }
        return false;
        }

        return true;
        }

        // Aplicar validación en tiempo real a los campos


        // Inicializar Flatpickr para el campo de fecha de registro
        flatpickr("#fecha_nacimiento", {
        dateFormat: "d-m-y",
                minDate: "1990-01-01", // Se puede ajustar según el rango que necesites
                maxDate: "today", // Para que no se pueda seleccionar fechas futuras
                defaultDate: "1990-01-01", // Fecha que aparece por defecto al abrir el calendario
                }); });
        