# FE0222B-BE_Progetto_settimana_11

FE0222B-BE_Progetto_settimana_11
Il Progetto e' stato fatto utilizzando spring

Per effettuare l'aggiunta di un utente bisogna passare -username -password -email -ed i ruoli come un set di stringhe, sceglibile tra user ed admin

Al momento swagger risulta essere inaccessibile per via di un problema con le autorizzazioni

Mentre i test riesco a fare le chiamate a buon fine se passati i valori giusti, ma danno sempre errore nella console dei test

l'aggiunta di category ha bisogno di -name

l'aggiunta di author ha bisogno di -name -surname

l'aggiunta di book ha bisogno di -name, -un set di autori(se i valori passati risultano non esistenti nel database vengono aggiunti evitando duplicati), -un set di categorie(se i valori passati risultano non esistenti nel database vengono aggiunti evitando duplicati),-un prezzo, la data e' opzionale e deve essere definita con il formato yyyy-mm-gg
