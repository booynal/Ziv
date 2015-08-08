var org_data = {
    "2014": {
        "语文": "CDACADBDCBDDACBCBCABDBCAABCACCAABCDDDCABADDBBDCBDB",
        "数学": "ABCDCDAABDBBADDCADCBABCDA",
        "逻辑": "BADACCABBDACDCBCBDDBAACCDBDDAACCBADADDACBCDABACBDA",
        "英语": "BDCDCACBABABBCACCADDDDACBACCACBDCAADDBDBCBAABDCDBC"
    },
    "2013": {
        "语文": "AABDABDAABDDCCDBCAADCBBAACBDDCDABBCAACCBADACACACDA",
        "数学": "CDDCBACBBDCDCAAADBABBCDBA",
        "逻辑": "CDCADBDCDCBBDCABBBBDAACABDCDCBBDAACBADBCABCCDCBADD",
        "英语": "CCDADBABACCBADACCABDCDABDADBBDADBCABBADCBDCAABDBAC"
    },
    "2012": {
        "语文": "CBCCBDBAACDDCACDCBCBBADDAADABACCBADDBCADADACBDBACB",
        "数学": "ADBDCDBCBADACDCBAAABDCBCD",
        "逻辑": "ABBDBCBCCADADCDCBDBBDACDBDABCBBACADCBCACBCBCDDACDB",
        "英语": "CACDABACCBBACDCCBBDDBBABABCDAABCADBCADAAABDBBCCDAB"
    },
    "2011": {
        "语文": "DDABDABDCBDBCBDBABADDACBCCABADCBADBCDBADBCDDACBDCA",
        "数学": "DBBCDADBACCDCABDBCACAABAD",
        "逻辑": "ACBABCADDBDCCBCDABDDCBBDAABCADCABCDBBDACABBCDDDCBB",
        "英语": "BCDCDBDDBADBDACBDAACDACBABDBCABCDACBDABDBCBBADAACD"
    },
    "2010": {
        "语文": "AACCDCBCBDBDCCBABABDBBDCBADBAAADDCDAADCACBCCCBBDCB",
        "数学": "ABCCDDCAABAADBDCBDDCCABDB",
        "逻辑": "DCCCBDDBDDBCBBCABCDDCDCCBCBABDACBCBDDABCBADDABACDB",
        "英语": "ADCCBABDABDABCBCADBADBADCCBAAAACBACDBADACCACABDBBA"
    },
    "2009": {
        "语文": "BACDBDDCAADACDCCBBDCCADDBABCBBADADCBDCBCBADDCAACDC",
        "数学": "CBABDCDACBACDCBCBDADABDCB",
        "逻辑": "BACDDACABCDABCABCDBAABDABCACACCBABDBABBDDCABBDACBD",
        "英语": "CBBDCDCADABACCBABCDBBDCAADABABDABCBCDACADACCDBCBDB"
    },
    "2008": {
        "语文": "BDDACDCDCAABDADDCCBCADBAADABCBBADDCCABCBACDCCBDDDC",
        "数学": "ADCDBBACABCDCCBBDDBBACABA",
        "逻辑": "CDBBCDABCDDBADACBADABABABCBBDDCBDDBDBCDCABCADDDCBC",
        "英语": "CBBAAAADACBDCDBCDBAADDAABBACCDAABBCADCAABAABBDDBCB"
    },
    "2007": {
        "语文": "ADBABCCDDCBABCACADBCDADCACBCDABCADBBDBACDBCDCBACBD",
        "数学": "CBBDBACACDABBDCCBADADDCDB",
        "逻辑": "BDBBBCCADCBCADDDBDAAABACCBDBABBDADACBAAAABCDADCCDA",
        "英语": "DBACCDCCDCBDACBBCDCDACADACDCADADCABBACDCBCADABAACC"
    },
    "2006": {
        "语文": "CBCCDDBBABDDCDBADDCCACBCDCCDBCBCDACCBCADCDACBDABCA",
        "数学": "CDBCBADABBACCADDACBABCDCB",
        "逻辑": "ACBABCADDBDCCBCDABDDCBBDAABCADCABCDBBDACABBCDDDCBB",
        "英语": "ACBADCCCABDCDABACADBACCDBACDBBDBAACDABDCCDACBCCBAC"
    },
    "2005": {
        "语文": "BBAACCDCDDABBBCBDBCADADDABCBCDDACDCADBDDACDABBBCCB",
        "数学": "ACDCBDCABBBACCBDDCACBDDAB",
        "逻辑": "CDCBCDCDBCADDDBDABDCBADCBCACAACCDABDBDAABBDDCDAADC",
        "英语": "ACDBBDBDCAACCDCDCBABABBABBAADAADCADDBADCACDDAABDAD"
    },
    "2004": {
        "语文": "CBDCDDABCCBDDBACDCDCCABCCCBDDBCDDCDACBDCABCCADCCAB",
        "数学": "BDCCDAABDCCCBABADBCBABDCC",
        "逻辑": "BBADDBBCCAAACBBDBDCCCDCADCDBCDABBADACBDABDCDADBCAD",
        "英语": "CCCDAADCACCDACAABDCDDDDDCDABBCBCDADDABADDBDCBDCDAB"
    },
    "2003": {
        "语文": "BDCCDBBABADADCBCBDABBDCCCBBDCAADDBACCBABDCDBDAACCB",
        "数学": "BCBDDCDAABDCCBABCCBDADAAD",
        "逻辑": "CBCACBADDCBBDCBCCBDACBCDBCDCDBCBCCCBBCCDBAACDDBAAC",
        "英语": "BCADCBACCBCDADBDCCBACBDABDBDCCBDADBAABCCBADCDBCCAA"
    },
};

function getDistributeChartData() {
    var answer2value = {'A': '10', 'B': '20', 'C': '30', 'D': '40',};

    var allData = [];
    for (var year in org_data) {
        var yearData = [];
        for (var rowid in org_data[year]) {
            var thisYearAnswerParts = org_data[year][rowid].split('');
            for (var columnid = 0; columnid < thisYearAnswerParts.length; columnid++)
                yearData.push({
                    'rowid': rowid,
                    'columnid': (columnid+1) + '',
                    'value': answer2value[thisYearAnswerParts[columnid]]});
        }
        allData.push({'year': year, 'data': yearData});
    }

    allData.sort(function(a,b) {return b.year-a.year});// 降序排
    return {'title': 'MultiYearGctAnswer', 'data': allData};
}

function getPieChartData() {
    var map2pieData = function(m) {
        var data = [];
        for (var label in m) {
            data.push({"label": label, "value": m[label]});
        }
        return data;
    };

    var allData = {};
    for (var year in org_data) {
        var yearData = {};
        var yearStat = {'A': 0, 'B': 0, 'C': 0, 'D': 0};
        for (var subj in org_data[year]) {
            var parts = org_data[year][subj].split('');
            var subjStat = {'A': 0, 'B': 0, 'C': 0, 'D': 0};
            for (var i = 0; i < parts.length; i++) {
                subjStat[parts[i]] += 1;
                yearStat[parts[i]] += 1;
            }
            yearData[subj] = map2pieData(subjStat);
        }
        yearData["汇总"] = map2pieData(yearStat);
        allData[year] = map2pieData(yearData);
    }
    
    return allData;
}











