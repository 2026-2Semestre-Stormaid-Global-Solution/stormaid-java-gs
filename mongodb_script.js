// =========================================================================
// STORMAID - SCRIPT MONGODB DE SUPORTE (GLOBAL SOLUTION)
// =========================================================================

// 1. Inserção de Cidadãos na Coleção 'citizens'
db.citizens.insertMany([
    { _id: 1, fullName: "Ana Souza", email: "ana.souza@email.com", district: "Pinheiros", dependentsCount: 2, vulnerabilityScore: 3 },
    { _id: 2, fullName: "Carlos Silva", email: "carlos.silva@email.com", district: "Moema", dependentsCount: 0, vulnerabilityScore: 1 },
    { _id: 3, fullName: "Mariana Oliveira", email: "mariana.ol@email.com", district: "Itaim Bibi", dependentsCount: 1, vulnerabilityScore: 4 },
    { _id: 4, fullName: "João Pereira", email: "joao.per@email.com", district: "Sé", dependentsCount: 3, vulnerabilityScore: 5 },
    { _id: 5, fullName: "Beatriz Lima", email: "beatriz.lima@email.com", district: "Pinheiros", dependentsCount: 1, vulnerabilityScore: 2 }
]);

// 2. Inserção de Incidentes na Coleção 'incidents'
db.incidents.insertMany([
    { _id: 101, citizenId: 1, category: "CLIMATE_DISASTER", description: "Alagamento severo na via principal", status: "PENDING", severity: "HIGH", timestamp: new Date() },
    { _id: 102, citizenId: 2, category: "STRUCTURAL", description: "Rachadura em muro de arrimo", status: "IN_PROGRESS", severity: "MEDIUM", timestamp: new Date() },
    { _id: 103, citizenId: 3, category: "MEDICAL", description: "Necessidade de resgate para idoso acamado", status: "DISPATCHED", severity: "HIGH", timestamp: new Date() },
    { _id: 104, citizenId: 4, category: "CLIMATE_DISASTER", description: "Queda de árvore bloqueando acesso", status: "RESOLVED", severity: "LOW", timestamp: new Date() },
    { _id: 105, citizenId: 5, category: "STRUCTURAL", description: "Deslizamento leve de terra em talude", status: "PENDING", severity: "HIGH", timestamp: new Date() },
    { _id: 106, citizenId: 1, category: "MEDICAL", description: "Falta de energia em equipamento médico", status: "IN_PROGRESS", severity: "HIGH", timestamp: new Date() }
]);

// 3. Consultas e Agregações Exigidas
print("--- INCIDENTES DE ALTA GRAVIDADE ---");
printjson(db.incidents.find({ severity: "HIGH" }).toArray());

print("--- CONTAGEM DE CIDADÃOS POR BAIRRO ---");
printjson(db.citizens.aggregate([
    { $group: { _id: "$district", totalCitizens: { $sum: 1 } } }
]).toArray());

print("--- TOTAL DE INCIDENTES POR CATEGORIA ---");
printjson(db.incidents.aggregate([
    { $group: { _id: "$category", totalIncidents: { $sum: 1 } } },
    { $sort: { totalIncidents: -1 } }
]).toArray());