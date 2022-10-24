# Senex

A tool to extract sentence-translation pairs from [Tatoeba](https://tatoeba.org).

## Usage

To use Sensex, you need only to provide a few key-value pairs. As an example, let's take a look at the key-value pairs Senex can be given in order to extract 10 pages of German-English sentence-translation pairs:

```
from:   deu
to:     eng
page:   [1..10]
```

To actually use Senex, the following command can be run:

```shell
$ senex "from: deu" "to: eng" "page: [1..10]"
```

Notice how Senex accepts both singular and multiple values; to extract the 2nd page each of German-English and French-English, one could run the following:

```shell
$ senex "from: [due, fra]" "to: eng" "page: 2"
```

Language codes can be found at [Tatoeba](https://tatoeba.org/).