"use client"

import { motion } from "framer-motion"
import { Sun, Cloud, Wind, Droplets, MapPin, Thermometer } from "lucide-react"

const cities = [
  { 
    name: "Marrakech", 
    temp: 32, 
    condition: "Sunny",
    humidity: 25,
    wind: 12,
    icon: Sun 
  },
  { 
    name: "Casablanca", 
    temp: 24, 
    condition: "Partly Cloudy",
    humidity: 65,
    wind: 18,
    icon: Cloud 
  },
  { 
    name: "Fes", 
    temp: 28, 
    condition: "Clear",
    humidity: 35,
    wind: 8,
    icon: Sun 
  },
]

export function WeatherWidget() {
  const mainCity = cities[0]

  return (
    <section className="px-4 py-6">
      <motion.div
        initial={{ opacity: 0, y: 20 }}
        animate={{ opacity: 1, y: 0 }}
        className="mb-4"
      >
        <h2 className="text-lg font-semibold">Weather</h2>
        <p className="text-xs text-muted-foreground">Current conditions across Morocco</p>
      </motion.div>

      {/* Main weather card */}
      <motion.div
        initial={{ opacity: 0, scale: 0.95 }}
        animate={{ opacity: 1, scale: 1 }}
        transition={{ delay: 0.2 }}
        className="relative overflow-hidden rounded-3xl bg-gradient-to-br from-secondary/20 via-primary/10 to-accent/20 border border-border/50 p-5 mb-4"
        whileHover={{ scale: 1.02 }}
      >
        {/* Animated sun glow */}
        <motion.div
          className="absolute -top-10 -right-10 w-40 h-40 rounded-full bg-secondary/30 blur-3xl"
          animate={{ 
            scale: [1, 1.2, 1],
            opacity: [0.3, 0.5, 0.3]
          }}
          transition={{ duration: 4, repeat: Infinity }}
        />

        <div className="relative flex items-start justify-between">
          <div>
            <div className="flex items-center gap-2 mb-2">
              <MapPin className="w-4 h-4 text-primary" />
              <span className="text-sm font-medium">{mainCity.name}</span>
            </div>
            <motion.div 
              className="flex items-start gap-1"
              initial={{ scale: 0.8 }}
              animate={{ scale: 1 }}
              transition={{ delay: 0.4, type: "spring" }}
            >
              <span className="text-5xl font-bold">{mainCity.temp}</span>
              <span className="text-2xl text-muted-foreground mt-1">°C</span>
            </motion.div>
            <p className="text-sm text-muted-foreground mt-1">{mainCity.condition}</p>
          </div>

          <motion.div
            animate={{ 
              rotate: [0, 10, -10, 0],
              scale: [1, 1.05, 1]
            }}
            transition={{ duration: 5, repeat: Infinity }}
          >
            <mainCity.icon className="w-16 h-16 text-secondary drop-shadow-lg" />
          </motion.div>
        </div>

        {/* Weather details */}
        <div className="flex gap-4 mt-4 pt-4 border-t border-border/30">
          <motion.div 
            className="flex items-center gap-2"
            initial={{ opacity: 0, y: 10 }}
            animate={{ opacity: 1, y: 0 }}
            transition={{ delay: 0.5 }}
          >
            <div className="w-8 h-8 rounded-lg bg-accent/20 flex items-center justify-center">
              <Droplets className="w-4 h-4 text-accent" />
            </div>
            <div>
              <p className="text-xs text-muted-foreground">Humidity</p>
              <p className="text-sm font-medium">{mainCity.humidity}%</p>
            </div>
          </motion.div>

          <motion.div 
            className="flex items-center gap-2"
            initial={{ opacity: 0, y: 10 }}
            animate={{ opacity: 1, y: 0 }}
            transition={{ delay: 0.6 }}
          >
            <div className="w-8 h-8 rounded-lg bg-primary/20 flex items-center justify-center">
              <Wind className="w-4 h-4 text-primary" />
            </div>
            <div>
              <p className="text-xs text-muted-foreground">Wind</p>
              <p className="text-sm font-medium">{mainCity.wind} km/h</p>
            </div>
          </motion.div>
        </div>
      </motion.div>

      {/* Other cities */}
      <div className="flex gap-3 overflow-x-auto pb-2 scrollbar-hide">
        {cities.slice(1).map((city, index) => (
          <motion.div
            key={city.name}
            initial={{ opacity: 0, x: -20 }}
            animate={{ opacity: 1, x: 0 }}
            transition={{ delay: 0.1 * index + 0.7 }}
            className="flex-shrink-0 flex items-center gap-3 px-4 py-3 rounded-xl bg-muted/30 border border-border/30 min-w-[160px]"
            whileHover={{ 
              scale: 1.05,
              borderColor: "oklch(0.65 0.18 45 / 0.5)"
            }}
          >
            <city.icon className="w-8 h-8 text-secondary" />
            <div>
              <p className="text-sm font-medium">{city.name}</p>
              <div className="flex items-center gap-1">
                <Thermometer className="w-3 h-3 text-muted-foreground" />
                <span className="text-sm text-muted-foreground">{city.temp}°C</span>
              </div>
            </div>
          </motion.div>
        ))}
      </div>
    </section>
  )
}
