import googlemaps
import os, configparser, requests, math, json

# load configuration file
dir_path = os.path.dirname(os.path.abspath(__file__))
config = configparser.ConfigParser()
config.read(os.path.join(dir_path, "config.ini"))
API_KEY = config.get("secret", "GOOGLE_MAP_API")

class GeoLocation:
    def __init__(self):
        self.gmaps = googlemaps.Client(key=API_KEY)

    def get_nearby_restaurants(self, latitude, longitude, radius=3000):
        endpoint_url = "https://maps.googleapis.com/maps/api/place/nearbysearch/json"
        params = {
            'location': f'{latitude},{longitude}',
            'radius': radius,
            'type': 'restaurant',
            'key': API_KEY
        }
        try:
            response = requests.get(endpoint_url, params=params)
            response.raise_for_status()
            restaurants = []
            data = response.json()
            for restaurant in data['results']:
                restaurant_latitude = restaurant['geometry']['location']['lat']
                restaurant_longitude = restaurant['geometry']['location']['lng']
                distance = haversine_distance(latitude, longitude, restaurant_latitude, restaurant_longitude)
                restaurants.append({'name':restaurant['name'],'rating':restaurant['rating'], 'distance':distance, 'place_id':restaurant['place_id']})
        except requests.RequestException as e:
            print(f"Error making the request: {e}", flush=True)
        except KeyError as e:
            print(f"Key error: {e}. The API response format might have changed or the provided place_id could be invalid.", flush=True)
        except Exception as e:
            print(f"An unexpected error occurred: {e}", flush=True)

        return restaurants

    def get_restaurant_details(self, place_id):
        endpoint_url = "https://maps.googleapis.com/maps/api/place/details/json"
        params = {
            'place_id': place_id,
            'key': API_KEY
        }

        try:
            response = requests.get(endpoint_url, params=params)
            response.raise_for_status()
            details = {}
            data = response.json()
            result = data.get('result', {})
            details["open_now"] = result.get('current_opening_hours', {}).get('open_now', "")
            details["address"] = result.get('formatted_address', "")
            details["phone_number"] = result.get('formatted_phone_number', "")
            details["reviews"] = ""

            # get most recent 5 reviews
            reviews = result.get('reviews', [])
            for i in range(min(5, len(reviews))):
                text = reviews[i].get('text', '')
                details["reviews"] += text
        
        except requests.RequestException as e:
            print(f"Error making the request: {e}", flush=True)
        except KeyError as e:
            print(f"Key error: {e}. The API response format might have changed or the provided place_id could be invalid.", flush=True)
        except Exception as e:
            print(f"An unexpected error occurred: {e}", flush=True)

        return details
    
    def get_city_name(self, latitude, longitude):
        result = self.gmaps.reverse_geocode((latitude, longitude))

        city = None
        for component in result[0]['address_components']:
            if 'political' in component['types'] and 'administrative_area_level_2' in component['types']:
                city = component['long_name']
                break

        return city

def haversine_distance(lat1, lon1, lat2, lon2):
    R = 6371.0

    dlat = math.radians(lat2 - lat1)
    dlon = math.radians(lon2 - lon1)

    a = (math.sin(dlat / 2) ** 2 +
         math.cos(math.radians(lat1)) * math.cos(math.radians(lat2)) * 
         math.sin(dlon / 2) ** 2)
    c = 2 * math.atan2(math.sqrt(a), math.sqrt(1 - a))

    distance = R * c 
    # round to 3 decimal places
    return int(distance*1000)

if __name__ == "__main__":
    geo = GeoLocation()
    print(geo.get_city_name(-37.800045, 144.965310))
    restaurants = geo.get_nearby_restaurants(-37.800045, 144.965310)
    print(restaurants[0])
    print(geo.get_restaurant_details(restaurants[2]['place_id']))

