#include <string>
#include <iostream>

using namespace std;

const int alphabet = 26;

string get_text();
void initialize_alphabet(char letter[], long count[]);
void count_occurances(string in, char letter[], long count[]);
void sort_occurances(char letter[], long count[]);
void output_occurances(char letter[], long count[]);

int main() {
  char letter[alphabet];
  long count[alphabet];


  initialize_alphabet(letter, count);

  cout << "Enter cypher text (<Enter><Ctrl+D> to end): " << endl;

  string input = get_text();
  count_occurances(input, letter, count);
  sort_occurances(letter, count);
  output_occurances(letter, count);

  return 0;
}

void initialize_alphabet(char letter[], long count[]) {
  char cur_char = 'a';
  for(int i = 0; i < alphabet; ++i) {
    letter[i] = cur_char++;
    count[i] = 0;
  }
}

string get_text() {
  string str;
  string acc;

  while(getline(cin, str)) {
    acc += str + '\n';
  }

  return acc;
}

void count_occurances(string in, char letter[], long count[]) {
  int len = in.length();

  for (int i = 0; i < len; ++i) {
    char c = tolower(in[i]);
    for (int j = 0; j < alphabet; ++j) {
      if (letter[j] == c) {
	++count[j];
      }
    }
  }
}

void sort_occurances(char letter[], long count[]) {
  for (int i = 0; i < alphabet; ++i) {
    int max_idx = i;
    int max = count[i];
    for (int j = i + 1; j < alphabet; ++j) {
      if (count[j] > max) {
	max_idx = j;
	max = count[j];
      }
    }

    char max_char = letter[max_idx];
    count[max_idx] = count[i];
    letter[max_idx] = letter[i];

    count[i] = max;
    letter[i] = max_char;
  }
}

void output_occurances(char letter[], long count[]) {
  cout << endl;

  for (int i = 0; i < alphabet; ++i) {
    if ( count[i] > 0 )
    cout << letter[i] << '\t' << count[i] << endl;
  }
}
