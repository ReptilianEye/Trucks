# Trucks

Aplikacja Trucks jest aplikacją konsolową, której jest system kontroli odprawy celnej. Zadaniem jej jest odpowiedni przydział przyjeżdżających ciężarówek do odpowiednich bram odpraw.

## Algorytm

Algorytm, który zaimplementowałem opiera się na możliwości jak największego wyborzu ciężarówek, które można przydzielić do bram odpraw. Aby to osiągnąć, zawsze w jednej z kolejek, zachowujemy 4 miejsca wolne (wszytkie oprócz aktualnie sprawdzanego). Dzięki temu każdą dostępną ciężarówke można przekierować do wolnej bramy odpraw. Dodatkowo ciężarówki, które już przyjechały, ale nie mieszczą się w kolejkach są zapisywane jako oczekujące. Z kolejki oczekujących mamy dostęp tylko do wagi pierwszej z oczekujących ciężarówek (zakładam, że miała już sprawdzone dokumenty). Dzięki temu, jeżeli brama 'towar' jest wolna, możemy wybrać zachłannie najlżejszą ciężarówkę z maksymalnie 5 dostępnych (4 w kolejce i 1 oczekująca). Jeśli wolna jest brama towarowa kolejki, która aktualnie jest pełna, to wybieramy najlżejszą ciężarówkę z kolejki, i oczekujących, a następnie przesuwamy wszystkie inne ciężarówki do drugiej z kolejek.

## Uruchomienie

Aby uruchomić aplikację należy sklonować repozytorium, a następnie w katalogu z projektem wykonać komendę:

```sh
java -jar build/libs/Trucks-1.0-SNAPSHOT.jar
```

lub w IntelliJ IDEA uruchomić plik Main.kt.

## Uwagi

- Aplikacja oprócz zczytywania z standardowego wejścia, czyta również z pliku `dane.in` w katalogu z projektem. TODO: W przyszłości można dodać możliwość podania ścieżki do pliku jako argumentu.
- Do testów jednostkowych zczytywania z pliku tworzę tymczasowy plik. Niestety pomimo prób, nie udało mi się usuwać tego pliku po zakończeniu testów. TODO: Poprawić usuwanie pliku tymczasowego.
- Wybór Kotlina nad Javą był spowodowany chęcią pozostania przy języku obiektowym, ale sprawdzenia nowych możliwości jakie daje Kotlin.
