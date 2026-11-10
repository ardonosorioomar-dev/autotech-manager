const AT = (() => {
    const ISV = 0.15;

    const estados = {
        'Recibida':                   { color: 'mute',   icon: '📥' },
        'En diagnóstico':             { color: 'blue',   icon: '🔍' },
        'Pendiente de aprobación':    { color: 'yellow', icon: '⏳' },
        'En reparación':              { color: 'amber',  icon: '🔧' },
        'En control de calidad':      { color: 'purple', icon: '✅' },
        'Finalizada y entregada':     { color: 'green',  icon: '🏁' },
        'Rechazada':                  { color: 'red',    icon: '❌' },
        'Cancelada':                  { color: 'red',    icon: '🚫' },
    };

    const metodospago = ['Efectivo', 'Tarjeta de Crédito', 'Tarjeta de Débito', 'Transferencia', 'Cheque'];

    let _db = null;

    function getDB() {
        if (_db) return _db;
        _db = {
            clientes: JSON.parse(localStorage.getItem('at_clientes') || 'null') || [
                { id: 1, identidad: '0801-1988-12450', nombreCompleto: 'Carlos Roberto Mendoza', telefono: '9876-5432', correo: 'carlos.mendoza@gmail.com', direccion: 'Col. Kennedy, Tegucigalpa', activo: true },
                { id: 2, identidad: '0801-1992-05421', nombreCompleto: 'María Elena Rodríguez', telefono: '9541-2365', correo: 'maria.rodriguez@hotmail.com', direccion: 'Col. Las Hadas, Tegucigalpa', activo: true },
                { id: 3, identidad: '0501-1985-09874', nombreCompleto: 'Transportes del Valle S.R.L.', telefono: '2234-8899', correo: 'gerencia@transportesvalle.hn', direccion: 'San Pedro Sula, Cortés', activo: true },
            ],
            vehiculos: JSON.parse(localStorage.getItem('at_vehiculos') || 'null') || [
                { id: 1, idCliente: 1, placa: 'HBC 4821', vin: 'MR0HA32G9F0012345', marca: 'Toyota', modelo: 'Hilux', anio: 2015, color: 'Gris Metálico', kilometraje: 185420, tipoCombustible: 'Diesel', activo: true },
                { id: 2, idCliente: 1, placa: 'HDA 1176', vin: '2HGFA16596H501892', marca: 'Honda', modelo: 'Civic', anio: 2016, color: 'Negro', kilometraje: 94200, tipoCombustible: 'Gasolina', activo: true },
                { id: 3, idCliente: 2, placa: 'HBR 7745', vin: 'JT2BF22K930129841', marca: 'Toyota', modelo: 'Corolla', anio: 2012, color: 'Blanco', kilometraje: 145000, tipoCombustible: 'Gasolina', activo: true },
            ],
            mecanicos: JSON.parse(localStorage.getItem('at_mecanicos') || 'null') || [
                { id: 1, identidad: '0801-1980-00123', nombreCompleto: 'Kevin Alexander Maradiaga', especialidad: 'Diagnóstico y Motores Diesel', telefono: '9988-7766', activo: true, disponible: true },
                { id: 2, identidad: '0801-1990-11223', nombreCompleto: 'Carlos Eduardo Ramos', especialidad: 'Transmisiones y Tren Delantero', telefono: '9711-2233', activo: true, disponible: true },
                { id: 3, identidad: '0801-1995-44556', nombreCompleto: 'José Antonio Hernández', especialidad: 'Electricidad Automotriz y Escaneo', telefono: '9655-4433', activo: true, disponible: true },
            ],
            servicios: JSON.parse(localStorage.getItem('at_servicios') || 'null') || [
                { id: 1, codigo: 'SRV-101', nombre: 'Cambio de Aceite y Filtro', descripcion: 'Sustitución de lubricante de motor y filtro universal', nivel: 1, categoria: 'Mantenimiento básico', costoManoObra: 450, horas: 1.0, activo: true },
                { id: 2, codigo: 'SRV-102', nombre: 'Cambio de Bujías e Inspección', descripcion: 'Reemplazo de bujías y cables de encendido', nivel: 1, categoria: 'Mantenimiento básico', costoManoObra: 500, horas: 1.5, activo: true },
                { id: 3, codigo: 'SRV-201', nombre: 'Afinamiento e Inyectores', descripcion: 'Limpieza de inyectores por ultrasonido y calibración', nivel: 2, categoria: 'Mantenimiento preventivo', costoManoObra: 1200, horas: 3.0, activo: true },
                { id: 4, codigo: 'SRV-202', nombre: 'Servicio Completo de Frenos', descripcion: 'Rectificación de discos y pastillas delanteras/traseras', nivel: 2, categoria: 'Mantenimiento preventivo', costoManoObra: 950, horas: 2.5, activo: true },
                { id: 5, codigo: 'SRV-301', nombre: 'Cambio de Radiador y Purga', descripcion: 'Sustitución de radiador y llenado de refrigerante 50/50', nivel: 3, categoria: 'Reparaciones intermedias', costoManoObra: 1500, horas: 4.0, activo: true },
                { id: 6, codigo: 'SRV-401', nombre: 'Reparación de Transmisión Automática', descripcion: 'Desmontaje, cambio de discos de embrague y sellos', nivel: 4, categoria: 'Reparaciones avanzadas', costoManoObra: 6500, horas: 14.0, activo: true },
                { id: 7, codigo: 'SRV-402', nombre: 'Cepillado y Reconstrucción de Culata', descripcion: 'Prueba hidrostática, cepillado y empaque nuevo', nivel: 4, categoria: 'Reparaciones avanzadas', costoManoObra: 5500, horas: 12.0, activo: true },
                { id: 8, codigo: 'SRV-501', nombre: 'Sustitución Completa de Motor', descripcion: 'Desmontaje de motor averiado, acople de motor nuevo, purga y pruebas de compresión', nivel: 5, categoria: 'Reparaciones mayores', costoManoObra: 12500, horas: 24.0, activo: true },
            ],
            repuestos: JSON.parse(localStorage.getItem('at_repuestos') || 'null') || [
                { id: 1, codigo: 'REP-ACE-01', nombre: 'Aceite Sintético 10W-30 (Galón)', categoria: 'Aceites', stock: 25, stockMin: 6, costo: 420, precio: 640, activo: true },
                { id: 2, codigo: 'REP-FLT-01', nombre: 'Filtro de Aceite Hilux/Corolla', categoria: 'Filtros', stock: 18, stockMin: 5, costo: 110, precio: 185, activo: true },
                { id: 3, codigo: 'REP-BUJ-01', nombre: 'Bujía de Iridio Bosch', categoria: 'Bujías', stock: 12, stockMin: 8, costo: 130, precio: 215, activo: true },
                { id: 4, codigo: 'REP-BAT-01', nombre: 'Batería 12V 650 CCA Bosch', categoria: 'Baterías', stock: 4, stockMin: 2, costo: 1950, precio: 2900, activo: true },
                { id: 5, codigo: 'REP-FRN-01', nombre: 'Pastillas de Freno Hilux Delanteras', categoria: 'Frenos', stock: 6, stockMin: 3, costo: 620, precio: 980, activo: true },
                { id: 6, codigo: 'REP-RAD-01', nombre: 'Radiador Aluminio Hilux 2012-2016', categoria: 'Radiadores', stock: 3, stockMin: 1, costo: 2400, precio: 3600, activo: true },
                { id: 7, codigo: 'REP-MOT-1KD', nombre: 'Motor 3.0L 1KD-FTV Diesel', categoria: 'Motores', stock: 2, stockMin: 1, costo: 48000, precio: 68000, activo: true },
                { id: 8, codigo: 'REP-MOT-2ZR', nombre: 'Motor 1.8L 2ZR-FE Corolla', categoria: 'Motores', stock: 1, stockMin: 1, costo: 28000, precio: 42000, activo: true },
            ],
            motores: JSON.parse(localStorage.getItem('at_motores') || 'null') || [
                { id: 1, idRepuesto: 7, serie: '1KD-883921-HN', codigo: '1KD-FTV', cilindrada: 3.0, combustible: 'Diesel', marcaComp: 'Toyota', modeloComp: 'Hilux', anioIni: 2012, anioFin: 2016, estado: 'Disponible', ubicacion: 'Bodega Rack-01', notas: 'Motor nuevo garantizado 0km' },
                { id: 2, idRepuesto: 7, serie: '1KD-883922-HN', codigo: '1KD-FTV', cilindrada: 3.0, combustible: 'Diesel', marcaComp: 'Toyota', modeloComp: 'Hilux', anioIni: 2012, anioFin: 2016, estado: 'Disponible', ubicacion: 'Bodega Rack-02', notas: 'Segundo motor disponible' },
                { id: 3, idRepuesto: 8, serie: '2ZR-551240-HN', codigo: '2ZR-FE', cilindrada: 1.8, combustible: 'Gasolina', marcaComp: 'Toyota', modeloComp: 'Corolla', anioIni: 2010, anioFin: 2015, estado: 'Disponible', ubicacion: 'Bodega Rack-05', notas: 'Compatible con Corolla, NO con Hilux' },
            ],
            ordenes: JSON.parse(localStorage.getItem('at_ordenes') || 'null') || [
                { id: 1, numero: 'ORD-2026-00001', idCliente: 1, idVehiculo: 1, idMecanico: 1, fechaIngreso: '2026-11-01T08:30', fechaCompromiso: '2026-11-15T17:00', fechaFin: null, km: 185420, sintomas: 'Sobrecalentamiento excesivo, pérdida de potencia, humo blanco y sonido metálico.', diagnostico: 'Fisura de bloque, baja compresión en cilindros 2 y 4. Requiere sustitución completa de motor.', estado: 'En reparación', servicios: [{ idServicio: 8, precio: 12500, horas: 24, sub: 12500 }], repuestosUsados: [{ idRepuesto: 7, cantidad: 1, precio: 68000, sub: 68000 }], subServicios: 12500, subRepuestos: 68000, descuento: 0, isv: 12075, total: 92575, saldo: 92575 },
                { id: 2, numero: 'ORD-2026-00002', idCliente: 1, idVehiculo: 2, idMecanico: 2, fechaIngreso: '2026-11-05T09:00', fechaCompromiso: '2026-11-07T12:00', fechaFin: null, km: 94200, sintomas: 'Luz de motor encendida, posible fallo en inyectores.', diagnostico: 'Código P0300. Bujías en mal estado, posible contaminación de inyectores.', estado: 'En diagnóstico', servicios: [], repuestosUsados: [], subServicios: 0, subRepuestos: 0, descuento: 0, isv: 0, total: 0, saldo: 0 },
                { id: 3, numero: 'ORD-2026-00003', idCliente: 2, idVehiculo: 3, idMecanico: 3, fechaIngreso: '2026-11-08T10:00', fechaCompromiso: '2026-11-09T16:00', fechaFin: '2026-11-09T15:00', km: 145000, sintomas: 'Revisión general y cambio de aceite programado.', diagnostico: 'Sin fallas detectadas. Mantenimiento preventivo estándar.', estado: 'Finalizada y entregada', servicios: [{ idServicio: 1, precio: 450, horas: 1, sub: 450 }], repuestosUsados: [{ idRepuesto: 1, cantidad: 1, precio: 640, sub: 640 }, { idRepuesto: 2, cantidad: 1, precio: 185, sub: 185 }], subServicios: 450, subRepuestos: 825, descuento: 0, isv: 191.25, total: 1466.25, saldo: 0 },
            ],
            presupuestos: JSON.parse(localStorage.getItem('at_presupuestos') || 'null') || [
                { id: 1, idOrden: 1, numero: 'PRE-2026-00001', subtotalServicios: 12500, subtotalRepuestos: 68000, porcDescuento: 0, montoDescuento: 0, porcISV: 15, montoISV: 12075, total: 92575, estado: 'Aprobado', obs: 'Presupuesto aprobado por el cliente. Incluye motor 1KD-FTV serie 1KD-883921-HN.', fecha: '2026-11-02' },
            ],
            pagos: JSON.parse(localStorage.getItem('at_pagos') || 'null') || [
                { id: 1, idOrden: 3, recibo: 'REC-2026-00001', monto: 1466.25, metodo: 'Efectivo', referencia: '', fecha: '2026-11-09T15:00', notas: 'Pago total en efectivo.' },
            ],
            cambiosMotor: JSON.parse(localStorage.getItem('at_cambiosMotor') || 'null') || [
                { id: 1, idOrden: 1, idVehiculo: 1, idMotorNuevo: 1, serieAnterior: '1KD-784219', serieNuevo: '1KD-883921-HN', causa: 'Fundición total por sobrecalentamiento. Deformación irrecuperable en cilindros 2 y 4.', destino: 'Almacén de Chatarra / Retorno al cliente para peritaje de seguro', pruebas: 'Compresión: Cil1=18bar, Cil2=18.5bar, Cil3=18.2bar, Cil4=18.1bar. Escaneo limpio sin códigos.', garantiaMeses: 12, garantiaKm: 20000, certificado: 'GAR-MOT-2026-883921', aprobado: false, fecha: '2026-11-01' },
            ],
            nextId: JSON.parse(localStorage.getItem('at_nextId') || 'null') || { clientes: 4, vehiculos: 4, ordenes: 4, presupuestos: 2, pagos: 2, cambiosMotor: 2 },
        };
        syncLoad(_db);
        return _db;
    }

    // ---- Conexion con la base de datos (Azure SQL). Sin servidor se usan los datos locales. ----
    const SYNC = ['clientes', 'vehiculos'];   // tablas Clientes y Vehiculos (api/sync)
    const GEN = ['mecanicos', 'servicios', 'repuestos', 'motores', 'ordenes', 'presupuestos', 'pagos', 'cambiosMotor']; // api/docs
    const ALL = SYNC.concat(GEN);
    function ruta(k) { return (SYNC.includes(k) ? 'api/sync/' : 'api/docs/') + k; }
    function peticion(metodo, k, cuerpo) {
        const x = new XMLHttpRequest();
        x.open(metodo, ruta(k), false);
        if (cuerpo) x.setRequestHeader('Content-Type', 'application/json');
        x.send(cuerpo || null);
        let r = null;
        try { r = JSON.parse(x.responseText); } catch (e) { }
        return { status: x.status, ok: x.status === 200 && Array.isArray(r), datos: r };
    }
    function syncLoad(db) {
        ALL.forEach(k => {
            try { const r = peticion('GET', k); if (r.ok) db[k] = r.datos; } catch (e) { }
        });
    }
    function syncSave(k) {
        const db = getDB();
        try {
            const r = peticion('POST', k, JSON.stringify(db[k]));
            if (r.ok) { db[k].splice(0, db[k].length, ...r.datos); return; }
            if (r.status === 0) return;   // sin servidor: queda solo en este navegador
            toast((r.datos && r.datos.error) || 'No se pudo guardar en la base de datos', 'error');
            const g = peticion('GET', k);
            if (g.ok) db[k].splice(0, db[k].length, ...g.datos);
        } catch (e) { }
    }

    function save(key) {
        const db = getDB();
        if (ALL.includes(key)) syncSave(key);
        const keyMap = {
            clientes: 'at_clientes', vehiculos: 'at_vehiculos', mecanicos: 'at_mecanicos',
            servicios: 'at_servicios', repuestos: 'at_repuestos', motores: 'at_motores',
            ordenes: 'at_ordenes', presupuestos: 'at_presupuestos', pagos: 'at_pagos',
            cambiosMotor: 'at_cambiosMotor', nextId: 'at_nextId'
        };
        if (key) {
            localStorage.setItem(keyMap[key], JSON.stringify(db[key]));
        } else {
            Object.keys(keyMap).forEach(k => localStorage.setItem(keyMap[k], JSON.stringify(db[k])));
        }
    }

    function nextId(entity) {
        if (SYNC.includes(entity)) return -Date.now(); // temporal: la base asigna el id real
        if (GEN.includes(entity)) return getDB()[entity].reduce((m, x) => Math.max(m, x.id || 0), 0) + 1;
        const db = getDB();
        if (!db.nextId[entity]) db.nextId[entity] = 1;
        return db.nextId[entity]++;
    }

    function L(n) {
        return 'L ' + Number(n).toLocaleString('es-HN', { minimumFractionDigits: 2, maximumFractionDigits: 2 });
    }

    function badge(estado) {
        const e = estados[estado] || { color: 'mute', icon: '?' };
        return `<span class="badge badge-${e.color}">${e.icon} ${estado}</span>`;
    }

    function nivelBadge(nivel) {
        const cols = ['', 'mute', 'blue', 'yellow', 'amber', 'red'];
        const labels = ['', 'Básico', 'Preventivo', 'Intermedio', 'Avanzado', 'Mayor'];
        return `<span class="badge badge-${cols[nivel]}">Niv. ${nivel} · ${labels[nivel]}</span>`;
    }

    function plate(p) {
        return `<span class="plate"><i>HONDURAS</i>${p}</span>`;
    }

    function toast(msg, type = 'default', dur = 3200) {
        const container = document.getElementById('toast-container') || (() => {
            const c = document.createElement('div');
            c.id = 'toast-container';
            c.className = 'toast-container';
            document.body.appendChild(c);
            return c;
        })();
        const t = document.createElement('div');
        t.className = `toast ${type !== 'default' ? 'toast-' + type : ''}`;
        t.innerHTML = `<span>${msg}</span>`;
        container.appendChild(t);
        setTimeout(() => { t.style.opacity = '0'; t.style.transform = 'translateX(30px)'; setTimeout(() => t.remove(), 300); }, dur);
    }

    function formatDate(str) {
        if (!str) return 'N/A';
        const d = new Date(str);
        return d.toLocaleDateString('es-HN', { day: '2-digit', month: '2-digit', year: 'numeric' });
    }

    function formatDateTime(str) {
        if (!str) return 'N/A';
        const d = new Date(str);
        return d.toLocaleDateString('es-HN', { day: '2-digit', month: '2-digit', year: 'numeric' }) + ' ' +
               d.toLocaleTimeString('es-HN', { hour: '2-digit', minute: '2-digit' });
    }

    function openModal(id) {
        const el = document.getElementById(id);
        if (el) el.classList.add('active');
    }

    function closeModal(id) {
        const el = document.getElementById(id);
        if (el) el.classList.remove('active');
    }

    function getClienteNombre(idCliente) {
        const db = getDB();
        const c = db.clientes.find(x => x.id === idCliente);
        return c ? c.nombreCompleto : 'Desconocido';
    }

    function getVehiculo(idVehiculo) {
        const db = getDB();
        return db.vehiculos.find(x => x.id === idVehiculo) || {};
    }

    function getMecanico(idMecanico) {
        const db = getDB();
        return db.mecanicos.find(x => x.id === idMecanico) || {};
    }

    function calcOrden(o) {
        const base = (o.subServicios + o.subRepuestos) - o.descuento;
        o.isv = +(base * ISV).toFixed(2);
        o.total = +(base + o.isv).toFixed(2);
        if (o.saldo === undefined) o.saldo = o.total;
        return o;
    }

    return { getDB, save, nextId, L, badge, nivelBadge, plate, toast, formatDate, formatDateTime, openModal, closeModal, getClienteNombre, getVehiculo, getMecanico, calcOrden, estados, metodospago, ISV };
})();

window.AT = AT;
