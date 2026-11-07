/* 
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Other/javascript.js to edit this template
 */



// ==========================================
// OAS Lite - Offline Attendance Storage
// IndexedDB Local Database
// ==========================================

const OAS_DB_NAME = "OAS_Offline_DB";
const OAS_DB_VERSION = 1;
const OAS_STORE_NAME = "pending_attendance";

// Open local database
function openOASDatabase() {

    return new Promise((resolve, reject) => {

        const request = indexedDB.open(
            OAS_DB_NAME,
            OAS_DB_VERSION
        );

        request.onupgradeneeded = function (event) {

            const db = event.target.result;

            if (!db.objectStoreNames.contains(OAS_STORE_NAME)) {

                const store = db.createObjectStore(
                    OAS_STORE_NAME,
                    { keyPath: "operationId" }
                );

                store.createIndex(
                    "syncStatus",
                    "syncStatus",
                    { unique: false }
                );

                store.createIndex(
                    "employeeId",
                    "employeeId",
                    { unique: false }
                );
            }
        };

        request.onsuccess = function () {
            resolve(request.result);
        };

        request.onerror = function () {
            reject(request.error);
        };

    });
}


// Save attendance operation locally
async function saveOfflineAttendance(employeeId, action) {

    const db = await openOASDatabase();

    const operation = {

        operationId: crypto.randomUUID(),

        employeeId: employeeId,

        action: action,

        recordedAt: new Date().toISOString(),

        syncStatus: "PENDING"

    };

    return new Promise((resolve, reject) => {

        const transaction = db.transaction(
            OAS_STORE_NAME,
            "readwrite"
        );

        transaction.objectStore(OAS_STORE_NAME)
            .add(operation);

        transaction.oncomplete = function () {
            db.close();
            resolve(operation);
        };

        transaction.onerror = function () {
            db.close();
            reject(transaction.error);
        };

    });
}


// Get all pending operations
async function getPendingAttendance() {

    const db = await openOASDatabase();

    return new Promise((resolve, reject) => {

        const transaction = db.transaction(
            OAS_STORE_NAME,
            "readonly"
        );

        const request = transaction.objectStore(
            OAS_STORE_NAME
        ).index("syncStatus").getAll("PENDING");

        request.onsuccess = function () {
            db.close();
            resolve(request.result);
        };

        request.onerror = function () {
            db.close();
            reject(request.error);
        };

    });
}


// Update operation sync status
async function updateAttendanceSyncStatus(
    operationId,
    status
) {

    const db = await openOASDatabase();

    return new Promise((resolve, reject) => {

        const transaction = db.transaction(
            OAS_STORE_NAME,
            "readwrite"
        );

        const store = transaction.objectStore(OAS_STORE_NAME);

        const request = store.get(operationId);

        request.onsuccess = function () {

            if (!request.result) {
                transaction.abort();
                return;
            }

            const operation = request.result;

            operation.syncStatus = status;

            store.put(operation);
        };

        transaction.oncomplete = function () {
            db.close();
            resolve(true);
        };

        transaction.onerror = function () {
            db.close();
            reject(transaction.error);
        };

    });
}


// Count pending operations
async function countPendingAttendance() {

    const operations = await getPendingAttendance();

    return operations.length;
}

