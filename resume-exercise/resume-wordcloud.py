from tika import parser
import os
import glob
from wordcloud import WordCloud,STOPWORDS
import matplotlib.pyplot as plt


def pdftotext (infile, outfile):
    # clear vars
    raw = ''
    text = ''
    # Load a file and extract information
    print ("Reading from " + infile)
    
    # parse text and make it a string
    raw = parser.from_file(infile)
    text = raw['content']
    text = str(text)

    # text to UTF8
    UTFtext = text.encode('utf-8', errors='ignore')
    # Replace \
    UTFtext = str(UTFtext).replace('\\', '\\\\').replace('"', '\\"')
    
    # Write content to output text file
    pdf = open(outfile, 'w')
    print ("Writing to " + outfile)
    pdf.write(text)
    pdf.close()

def wordcloud(data, color = 'black'):
    words = ' '.join(data)
    print(words)
    cleaned_word = " ".join([word for word in words.split()
                            if 'http' not in word
                                and not word.startswith('@')
                                and not word.startswith('#')
                                and word != 'RT'
                            ])

    wordcloud = WordCloud(stopwords=STOPWORDS,
                      background_color=color,
                      width=2500,
                      height=2000
                     ).generate(cleaned_word)
    plt.figure(1,figsize=(13, 13))
    plt.imshow(wordcloud)
    plt.axis('off')
    plt.show()

def main():
    # input and output dir
    inpath = 'data/input/myresume/' # 'data/input/myresume' for only my resume
    outpath = 'data/output/'
    # read ins and convert to outs
    for file in glob.glob(inpath + '*.pdf'):
        print("reading " + file)
        filename = os.path.basename(file).replace(".pdf","")
        outfile = outpath + filename + '.txt'
        pdftotext(file, outfile)
    #take outs and combine
    data = ''
    for file in glob.glob(outpath + '*.txt'):
        txtfile = open(file, 'r')
        content = str( txtfile.read()).split()
        contentstr = " ".join(content)
        data = data + contentstr
        txtfile.close()
    # clean output directory so new files can be input and word-clouded without interference from old files
    outfiles = os.listdir(outpath)
    for item in outfiles:
        os.remove(os.path.join(outpath, item))
    wordcloud(data.split())


if __name__ == "__main__":
    main()