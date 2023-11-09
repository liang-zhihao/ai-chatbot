from common import get_config
import requests
from imgurpython import ImgurClient

config = get_config()
secret = config.get("imgur","SECRET")
client_id = config.get("imgur","ID")

class images:
    def __init__(self):
        self.client = ImgurClient(client_id, secret)
    
    def generate_access_token(self):
        ...
    
    def upload_image(self, image):
        url = "https://api.imgur.com/3/image"
        payload={'image': image}
        files=[]
        headers = {
        'Authorization': 'Client-ID '+client_id
        }
        response = requests.request("POST", url, headers=headers, data=payload, files=files)
        print(response.text)

if __name__ == "__main__":
    img = images()
    img.test()