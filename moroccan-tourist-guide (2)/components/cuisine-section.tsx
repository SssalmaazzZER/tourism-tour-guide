"use client"

import { motion } from "framer-motion"
import { Utensils, Star, Clock, Flame, ChevronRight } from "lucide-react"

const cuisineItems = [
  {
    id: 1,
    name: "Tagine",
    description: "Slow-cooked Moroccan stew with tender meat and aromatic spices",
    cookTime: "2-3 hours",
    spiceLevel: 2,
    popular: true,
    image: "https://images.unsplash.com/photo-1541518763669-27fef9c59f0f?w=400&q=80",
  },
  {
    id: 2,
    name: "Couscous",
    description: "Traditional Friday dish with steamed semolina and seven vegetables",
    cookTime: "1.5 hours",
    spiceLevel: 1,
    popular: true,
    image: "https://images.unsplash.com/photo-1585937421612-70a008356fbe?w=400&q=80",
  },
  {
    id: 3,
    name: "Pastilla",
    description: "Sweet and savory pie with pigeon or chicken, wrapped in warqa pastry",
    cookTime: "2 hours",
    spiceLevel: 1,
    popular: false,
    image: "https://images.unsplash.com/photo-1606471191009-63994c53433b?w=400&q=80",
  },
  {
    id: 4,
    name: "Harira",
    description: "Traditional soup with tomatoes, lentils, and chickpeas",
    cookTime: "1 hour",
    spiceLevel: 2,
    popular: true,
    image: "https://images.unsplash.com/photo-1547592166-23ac45744acd?w=400&q=80",
  },
]

export function CuisineSection() {
  return (
    <section className="px-4 py-6">
      <motion.div
        initial={{ opacity: 0, y: 20 }}
        animate={{ opacity: 1, y: 0 }}
        className="flex items-center justify-between mb-4"
      >
        <div className="flex items-center gap-2">
          <Utensils className="w-5 h-5 text-primary" />
          <div>
            <h2 className="text-lg font-semibold">Moroccan Cuisine</h2>
            <p className="text-xs text-muted-foreground">A feast for the senses</p>
          </div>
        </div>
        <motion.button
          className="flex items-center gap-1 text-xs text-primary font-medium"
          whileHover={{ scale: 1.05, x: 3 }}
        >
          All dishes <ChevronRight className="w-4 h-4" />
        </motion.button>
      </motion.div>

      <div className="space-y-3">
        {cuisineItems.map((item, index) => (
          <motion.div
            key={item.id}
            initial={{ opacity: 0, x: -20 }}
            animate={{ opacity: 1, x: 0 }}
            transition={{ delay: 0.1 * index + 0.2 }}
            className="flex gap-4 p-3 rounded-2xl bg-muted/20 border border-border/30 overflow-hidden"
            whileHover={{ 
              scale: 1.02,
              borderColor: "oklch(0.65 0.18 45 / 0.5)",
              boxShadow: "0 10px 30px oklch(0 0 0 / 0.15)"
            }}
            whileTap={{ scale: 0.98 }}
          >
            {/* Image */}
            <motion.div 
              className="relative w-24 h-24 rounded-xl overflow-hidden flex-shrink-0"
              whileHover={{ scale: 1.05 }}
            >
              <img
                src={item.image}
                alt={item.name}
                className="w-full h-full object-cover"
              />
              {item.popular && (
                <div className="absolute top-1 left-1 px-1.5 py-0.5 rounded-full bg-secondary/90 flex items-center gap-0.5">
                  <Star className="w-2.5 h-2.5 fill-background text-background" />
                  <span className="text-[8px] font-medium text-background">Popular</span>
                </div>
              )}
            </motion.div>

            {/* Content */}
            <div className="flex-1 min-w-0">
              <h3 className="font-semibold mb-1">{item.name}</h3>
              <p className="text-xs text-muted-foreground line-clamp-2 mb-2">
                {item.description}
              </p>
              <div className="flex items-center gap-3">
                <span className="flex items-center gap-1 text-xs text-muted-foreground">
                  <Clock className="w-3 h-3" />
                  {item.cookTime}
                </span>
                <span className="flex items-center gap-0.5">
                  {[...Array(3)].map((_, i) => (
                    <Flame
                      key={i}
                      className={`w-3 h-3 ${
                        i < item.spiceLevel ? "text-primary fill-primary" : "text-muted/50"
                      }`}
                    />
                  ))}
                </span>
              </div>
            </div>

            {/* Action */}
            <motion.button
              className="self-center w-10 h-10 rounded-xl bg-primary/10 flex items-center justify-center"
              whileHover={{ scale: 1.1, backgroundColor: "oklch(0.65 0.18 45 / 0.3)" }}
              whileTap={{ scale: 0.9 }}
            >
              <ChevronRight className="w-5 h-5 text-primary" />
            </motion.button>
          </motion.div>
        ))}
      </div>
    </section>
  )
}
