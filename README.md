# GObank

GObank este proiectat pentru a oferi utilizatorilor funcționalitățile esențiale ale unui sistem bancar. Utilizatorii pot crea conturi noi, se pot autentifica, și pot gestiona diferite aspecte ale conturilor lor bancare, inclusiv soldurile, depunerile, retragerile și transferurile de fonduri. De asemenea, pot vizualiza istoricul tranzacțiilor pentru a urmări toate activitățile financiare. 

## 10 Acțiuni/Interogări

1. **Creare cont nou** - signup din UserRepository
2. **Autentificare utilizator** - login din UserRepository
3. **Schimbare parolă** - changePassById din UserRepository
4. **Vizualizare sold cont** - ShowMenu(userId) din BankAccountsMenu - afiseaza toate conturile bancare asociate si soldul lor
5. **Deschidere cont bancar nou** - newBankAccount din BankAccountRepository
6. **Vizualizare conturi bancare** - showMenu(userId) din BankAccountsMenu 
7. **Depunere** - deposit din BankAccountsMenu
8. **Retragere fonduri** - Withdrawal din BankAccountsMenu
9. **Transfer de fonduri** - Transfer din BankAccountsMenu
10. **Vizualizare istoric tranzacții** - transactionHistory - TransactionRepository

Tranzactiile sunt stocate intr-un TreeSet pentru a fi ordonate in functie de timestamp-ul lor
Valuta/conturile bancare sunt stocate intr-un map.
Conturile bancare se afla si intr-un hashmap pentru a putea accesa mai usor prin id-ul lor referinta contului.

## Obiecte

- **BankAccount**
- **Currency**
- **GoMenu**
- **Menu**
- **Transaction**
- **Transfer**
- **Withdrawal**
- **Deposit**

