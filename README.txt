Mini-projekt 5 - Mateusz Oleksik (s29325)
Wykonano: 
=> Wyświetlenie dowolnej strony w domyślnej przeglądarce. [2 pkt]
=> Wyświetlenie obrazka w ImageView Widgetu wraz z możliwością jego zmiany (minimum 2 obrazki). [4 pkt]
=> Odtworzenie dźwięku z możliwością, zatrzymywania, zakończenia oraz przejścia do kolejnego (minimum 2 dźwięki/utwory). [4 pkt]

Wszystkie funkcjonalności wykonuje się na widget'cie aplikacji

-------------------------------------
Wyświetlenie dowolnej strony w domyślnej przeglądarce:

=> Przycisk "Open webpage"

-> CustomWidget.kt
-> updateAppWidget() l.103 - l.104 (stworzenie intentu akcji i przypisanie do przycisku)
-> onReceive() l.38 - l.44 (wywołanie intentu ACTION_VIEW dla uri https://pja.edu.pl/)
-------------------------------------
Wyświetlenie obrazka w ImageView Widgetu wraz z możliwością jego zmiany:

=> Przycisk "Switch image" oraz ImageView powyżej niego

-> CustomWidget.kt
-> updateAppWidget() l.107 - l.108 (stworzenie intentu akcji i przypisanie do przycisku)
-> onReceive() l.47 - l.69 (ustawienie nowego drawable dla remote view image view i akutalizacjia widgetu poprzez AppWidgetManager)
-------------------------------------
Odtworzenie dźwięku z możliwością, zatrzymywania, zakończenia oraz przejścia do kolejnego:

=> Przyciski: 
	"Play/Pause" (odtwórz/zatrzymaj)
	"Stop" (zakończ)
	"Previous" (poprzedni utwór)
	"Next" (następny utwór) 

-> CustomWidget.kt
-> updateAppWidget() l.111 - l.124 (stworzenie intent'ów akcji dla każdej operacji [odtwórz, następny utwór itd.] i przypisanie do odpowiednich przycisków)
-> onReceive() l.73 - l.91
-> scheduleMusicPlayerJob() (uruchamia/zaczyna prace JobService odpowiedzialnego za kontrolę nad MediaPlayer)
-> MusicPlayerService.kt
-> Serwis ten przechowuje jedną instancję MediaPlayer i wykonuje wszystkie operacje (odwtórz, następny utwór itd.)
w zależności od tego jaka flaga akcji (zwykły string extras) została przekazana w JobParameters.

Link do repozytorium (w razie problemów z zip): https://github.com/MattOlecs/SMB_5