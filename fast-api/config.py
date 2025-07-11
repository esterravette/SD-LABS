import configparser

config = configparser.ConfigParser()

config['DEFAULT'] = {
    'arquivo': 'estoque.xml',
}

with open('config.ini', 'w') as configfile:
    config.write(configfile)
