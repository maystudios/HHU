#!/bin/bash

gradlecommand=$1

echo running style checks

$gradlecommand --console=rich checkstyleMain -Pstyle=braces.xml > checkstyle_braces.log 2>&1
echo -n .
$gradlecommand --console=rich checkstyleMain -Pstyle=camelcase.xml > checkstyle_camelcase.log 2>&1
echo -n .
$gradlecommand --console=rich checkstyleMain -Pstyle=indent2.xml > checkstyle_indent2spaces.log 2>&1
echo -n .
$gradlecommand --console=rich checkstyleMain -Pstyle=indent4.xml > checkstyle_indent4spaces.log 2>&1
echo -n .
$gradlecommand --console=rich checkstyleMain -Pstyle=indent8.xml > checkstyle_indent8spaces.log 2>&1
echo -n .
$gradlecommand --console=rich checkstyleMain -Pstyle=indenttab.xml > checkstyle_indenttab.log 2>&1
echo -n .
$gradlecommand --console=rich checkstyleMain -Pstyle=statementperline.xml > checkstyle_statementperline.log 2>&1
echo .

echo finished running style checks

indent=`du -b *indent*log | sort -n | head -n1 | cut -f 2`
style=`echo $indent | sed "s/.*_indent//" | sed "s/.log//"`

echo -e "\033[1mErkannter Einrückungsstil: $style\033[0m"

filepaths=(
    "checkstyle_braces.log"
    "checkstyle_camelcase.log"
    "checkstyle_statementperline.log"
    "$indent"
)

passedStyleChecks=0

for file in "${filepaths[@]}"; do
    title=`echo $file | sed "s/.*_//" | sed "s/.log//"`
    if grep -q "BUILD SUCCESSFUL" "$file"; then
        echo -e "$title \033[1;32mPASSED\033[0m"
        let passedStyleChecks++
    else
        echo -e "$title \033[1;31mFAILED\033[0m, reasons are given near »[ant:checkstyle] [WARN]«:"
        cat "$file"
    fi
done

# das sollten wir einfach NIE direkt in die Gesamtpunktzahl stecken:
# - verwirrt Studis weniger (Funktional vs. Stil)
# - gleicher Testcode, egal ob das gerade gewertet wird oder nicht
# Deshalb NICHT nicht Test.*Punkt in der Ausgabe haben! Wird sonst als regulärer Punkt geparst.
if [ $passedStyleChecks -eq ${#filepaths[@]} ]; then
    echo -e 'checkstyle > Style Checks \033[1;32mPASSED\033[0m' | tee -a tests.log
    exit 0
else
    echo -e 'checkstyle > Style Checks \033[1;31mFAILED\033[0m, see messages above (near »[ant:checkstyle] [WARN]«)' | tee -a tests.log
    exit 1
fi

