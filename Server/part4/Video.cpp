#include <iostream>
#include <string>
#include <vector>
using namespace std;

class Video {
    string videoId;
    vector<string> suggestions;

    public:
        // Constructor that initializes VideoId and ensures suggestions is an empty vector by default
        Video(const string& id, const vector<string>& sugg = {})
            : videoId(id), suggestions(sugg) {}

        // Getter for videoId
        string getVideoId() const {
            return videoId;
        }

        // Getter for suggestions
        vector<string> getSuggestions() const {
            return suggestions;
        }

        // Add a suggestion to the suggestions list of the video
        void addSuggestion(const string& id) {
            suggestions.push_back(id);
        }


};