import React, { useState } from 'react';
import SearchComponent from './components/SearchComponent';
import WeatherDisplayComponent from './components/WeatherDisplayComponent';
import RegionGalleryComponent from './components/RegionGalleryComponent';

interface WeatherRecord {
  id: number;
  cityName: string;
  region: string;
  latitude: number;
  longitude: number;
  temperature: number;
  timestamp: number;
}

const App: React.FC = () => {
  const [mainCity, setMainCity] = useState<WeatherRecord | null>(null);
  const [galleryCities, setGalleryCities] = useState<WeatherRecord[]>([]);
  const [searchMode, setSearchMode] = useState<'city' | 'region'>('city');

  const handleSearch = async (query: string, type: 'city' | 'region') => {
    setSearchMode(type);
    const endpoint = `http://localhost:8080/weather?${type === 'city' ? 'cityName' : 'region'}=${query}`;

    try {
      const response = await fetch(endpoint);

      if (response.status === 404) {
        alert("City Not Recorded");
        return;
      }

      const data: WeatherRecord[] = await response.json();

      if (data.length === 0) {
        alert("No results found.");
        return;
      }

      if (type === 'city') {
        // Requirement: Display searched city in Full Detail
        // and other cities in the same region in Minimal Detail
        const target = data[0];
        setMainCity(target);

        // Fetch all cities in that region for the gallery
        const regionResponse = await fetch(`http://localhost:8080/weather?region=${target.region}`);
        const regionData: WeatherRecord[] = await regionResponse.json();
        setGalleryCities(regionData.filter(c => c.id !== target.id));
      } else {
        // Requirement: Display all cities in region with minimal detail
        setMainCity(null);
        setGalleryCities(data);
      }
    } catch (error) {
      console.error("Error fetching weather data:", error);
    }
  };

  return (
    <div className="app-container">
      <h1>Weather Sensor Dashboard</h1>

      <SearchComponent onSearch={handleSearch} />

      <hr />

      {/* Full Detail Section */}
      {mainCity && (
        <section>
          <h2>Primary Search Result</h2>
          <WeatherDisplayComponent city={mainCity} isFullDetail={true} />
        </section>
      )}

      {/* Minimal Detail Gallery Section */}
      {galleryCities.length > 0 && (
        <section>
          <h2>{searchMode === 'city' ? `Other Cities in ${mainCity?.region}` : 'Region Results'}</h2>
          <RegionGalleryComponent cities={galleryCities} />
        </section>
      )}
    </div>
  );
};

export default App;