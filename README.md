# FlareVisualizer
an android app to track pain by the hour and summarize your flare


Flair data structures in JSON format:

for each flare, a summary object with avg, length, start, and end:
{ 
    "flares": {
        "1": {
            "pain_avg": int (average of reported numbers),
            "length": int (hours),
            "start":"date and time" (date will be formatted as MM-DD-YYYY, time will be 24hr clock),
            "end":"date and time"
        }
        "2":{...}
        "3":{...}
    }
}

for each flare, a second detailed object that contains hourly pain numbers and trigger list:
{
    "1": {
        "pain_nums_length": int (num of entries per flare),
        "pain_nums": {
            {
                "num": int (pain number reported),
                "time": int (in military time 0000-2300 hourly )
            },
            {...},
            {...},
        },
        "trigger_list": {
            {
                "trig": "string" (trigger associated with current flare)
            },
            {
                "trig": "string"
            },
            {...},
        }
    }
    "2": {...}
}

for each trigger list, each trigger has some associated numbers
{
    "activity": {
         {
             "trigger": "string" (trigger added to list),
             "freq": int (add the avg pain number for each flare associated with trigger),
         },
         {...},
         {...},
    }
}

{
    "diet": {
        {
            "trigger": "string",
            "freq": int
        },
        {...},
        {...},
    {
}

{
    "misc": {
        {
            "trigger":"string",
            "freq": int
        },
        {...},
        {...},
    }
}
