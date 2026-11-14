# fabrick-test-api
App di test

## Indice
1. Configurazione
2. Collection Postman

## Configurazione

La chiave API Fabrick non è salvata nel repository: viene letta dalla variabile d'ambiente `FABRICK_API_KEY` (vedi `fabrick.api-key` in `src/main/resources/application.yml`). Impostala prima dell'avvio, altrimenti l'applicazione non parte:

```bash
export FABRICK_API_KEY=<la-tua-api-key>
mvn spring-boot:run
```

Su Windows (PowerShell): `$env:FABRICK_API_KEY="<la-tua-api-key>"`.

##  Collection Postman

Collection Postman utili per richiamare le api: [Collection Postman](MyFabrickApi.postman_collection.json)
