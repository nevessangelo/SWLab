import Partition as partition

def main():

    choice ='0'
    while choice =='0':
        print("Main Choice: Choose 1 of 5 choices")
        print("1- Similarity of Cosine")
        print("2- Similarity of TI")
        print("3- Export MultiClass File to JRIP")
        print("4- Export MultiClass File to J48")
        print("5- Naive Bayes")
        print("6- Similarity Social Network")

        choice = input ("Please make a choice: ")
    
    if choice == "6":
        type = 'SN'
        entrada = (input('Digite qual a entrada: '))
        partition.Partition(entrada,type)
    if choice == "5":
        type = 'naive'
        entrada = (input('Digite qual a entrada: '))
        partition.Partition(entrada,type)
    elif choice == "4":
        type = 'J48'
        entrada = (input('Digite qual a entrada: '))
        partition.Partition(entrada,type)
    elif choice == "3":
        type = 'JRIP'
        entrada = (input('Digite qual a entrada: '))
        partition.Partition(entrada,type)
    elif choice == "2":
        type = 'TI'
        entrada = (input('Digite qual a entrada: '))
        partition.Partition(entrada,type)
    elif choice == "1":
        type = 'cosseno'
        entrada = (input('Digite qual a entrada: '))
        partition.Partition(entrada,type)
    else:
        print("I don't understand your choice.")


if __name__ == '__main__':
    main()
    print ("The End")
    