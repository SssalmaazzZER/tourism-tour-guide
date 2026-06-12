"use client"

import { motion } from "framer-motion"
import { Star, Heart, MapPin, Clock, ChevronRight } from "lucide-react"
import { useState } from "react"

const destinations = [
  {
    id: 1,
    name: "Jemaa el-Fna",
    location: "Marrakech",
    rating: 4.9,
    reviews: 2840,
    duration: "2-3 hours",
    image: "https://images.unsplash.com/photo-1539020140153-e479b8c22e70?w=600&q=80",
    tags: ["Historic", "Markets", "Night Life"],
    gradient: "from-primary/80 to-transparent"
  },
  {
    id: 2,
    name: "Hassan II Mosque",
    location: "Casablanca",
    rating: 4.8,
    reviews: 3150,
    duration: "1-2 hours",
    image: "https://images.unsplash.com/photo-1569383746724-6f1b882b8f46?w=600&q=80",
    tags: ["Architecture", "Religious", "Iconic"],
    gradient: "from-accent/80 to-transparent"
  },
  {
    id: 3,
    name: "Chefchaouen",
    location: "Blue City",
    rating: 4.9,
    reviews: 1920,
    duration: "Full day",
    image: "https://images.unsplash.com/photo-1553244933-82b1e0c6d3fa?w=600&q=80",
    tags: ["Photography", "Mountains", "Artisan"],
    gradient: "from-majorelle/80 to-transparent"
  },
  {
    id: 4,
    name: "Sahara Desert",
    location: "Merzouga",
    rating: 5.0,
    reviews: 4200,
    duration: "2-3 days",
    image: "https://images.unsplash.com/photo-1509023464722-18d996393ca8?w=600&q=80",
    tags: ["Adventure", "Camping", "Camels"],
    gradient: "from-secondary/80 to-transparent"
  },
]

export function FeaturedDestinations() {
  const [likedItems, setLikedItems] = useState<number[]>([])

  const toggleLike = (id: number) => {
    setLikedItems(prev => 
      prev.includes(id) ? prev.filter(i => i !== id) : [...prev, id]
    )
  }

  return (
    <section className="py-6">
      <motion.div
        initial={{ opacity: 0, y: 20 }}
        animate={{ opacity: 1, y: 0 }}
        className="px-4 flex items-center justify-between mb-4"
      >
        <div>
          <h2 className="text-lg font-semibold">Featured Destinations</h2>
          <p className="text-xs text-muted-foreground">Handpicked gems of Morocco</p>
        </div>
        <motion.button
          className="flex items-center gap-1 text-xs text-primary font-medium"
          whileHover={{ scale: 1.05, x: 3 }}
          whileTap={{ scale: 0.95 }}
        >
          View all <ChevronRight className="w-4 h-4" />
        </motion.button>
      </motion.div>

      {/* Horizontal scroll container */}
      <div className="overflow-x-auto scrollbar-hide">
        <motion.div 
          className="flex gap-4 px-4 pb-2"
          initial={{ opacity: 0, x: -20 }}
          animate={{ opacity: 1, x: 0 }}
          transition={{ delay: 0.2 }}
        >
          {destinations.map((dest, index) => (
            <motion.div
              key={dest.id}
              className="relative flex-shrink-0 w-[280px] rounded-3xl overflow-hidden bg-card border border-border/50"
              initial={{ opacity: 0, x: -30 }}
              animate={{ opacity: 1, x: 0 }}
              transition={{ delay: 0.1 * index + 0.3 }}
              whileHover={{ 
                scale: 1.02, 
                y: -8,
                boxShadow: "0 20px 40px oklch(0 0 0 / 0.3)"
              }}
              whileTap={{ scale: 0.98 }}
            >
              {/* Image container */}
              <div className="relative h-[180px] overflow-hidden">
                <motion.img
                  src={dest.image}
                  alt={dest.name}
                  className="w-full h-full object-cover"
                  whileHover={{ scale: 1.1 }}
                  transition={{ duration: 0.4 }}
                />
                {/* Gradient overlay */}
                <div className={`absolute inset-0 bg-gradient-to-t ${dest.gradient}`} />
                
                {/* Like button */}
                <motion.button
                  className="absolute top-3 right-3 w-9 h-9 rounded-full bg-background/20 backdrop-blur-sm flex items-center justify-center"
                  onClick={() => toggleLike(dest.id)}
                  whileHover={{ scale: 1.15 }}
                  whileTap={{ scale: 0.9 }}
                >
                  <Heart 
                    className={`w-5 h-5 transition-colors ${
                      likedItems.includes(dest.id) 
                        ? "fill-red-500 text-red-500" 
                        : "text-white"
                    }`} 
                  />
                </motion.button>

                {/* Rating badge */}
                <div className="absolute top-3 left-3 flex items-center gap-1 px-2 py-1 rounded-full bg-background/20 backdrop-blur-sm">
                  <Star className="w-3 h-3 fill-secondary text-secondary" />
                  <span className="text-xs font-medium text-white">{dest.rating}</span>
                </div>
              </div>

              {/* Content */}
              <div className="p-4">
                <h3 className="font-semibold mb-1">{dest.name}</h3>
                <div className="flex items-center gap-3 text-xs text-muted-foreground mb-3">
                  <span className="flex items-center gap-1">
                    <MapPin className="w-3 h-3" />
                    {dest.location}
                  </span>
                  <span className="flex items-center gap-1">
                    <Clock className="w-3 h-3" />
                    {dest.duration}
                  </span>
                </div>

                {/* Tags */}
                <div className="flex gap-2">
                  {dest.tags.map(tag => (
                    <span 
                      key={tag}
                      className="px-2 py-0.5 rounded-full bg-muted/50 text-[10px] text-muted-foreground"
                    >
                      {tag}
                    </span>
                  ))}
                </div>
              </div>
            </motion.div>
          ))}
        </motion.div>
      </div>
    </section>
  )
}
