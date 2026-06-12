"use client"

import { motion } from "framer-motion"
import { MapPin, Building2, Utensils, Mountain, Compass, Camera, Trophy, Music } from "lucide-react"

const categories = [
  { 
    icon: Building2, 
    label: "Medinas", 
    color: "from-primary to-secondary",
    count: "12+"
  },
  { 
    icon: Utensils, 
    label: "Cuisine", 
    color: "from-secondary to-saffron",
    count: "50+"
  },
  { 
    icon: Mountain, 
    label: "Adventure", 
    color: "from-emerald to-accent",
    count: "25+"
  },
  { 
    icon: MapPin, 
    label: "Landmarks", 
    color: "from-accent to-majorelle",
    count: "30+"
  },
  { 
    icon: Trophy, 
    label: "Football", 
    color: "from-emerald to-primary",
    count: "8+"
  },
  { 
    icon: Camera, 
    label: "Photo Spots", 
    color: "from-primary to-accent",
    count: "40+"
  },
  { 
    icon: Music, 
    label: "Culture", 
    color: "from-majorelle to-primary",
    count: "20+"
  },
  { 
    icon: Compass, 
    label: "Tours", 
    color: "from-secondary to-emerald",
    count: "15+"
  },
]

export function CategoryGrid() {
  return (
    <section className="px-4 py-6">
      <motion.div
        initial={{ opacity: 0, y: 20 }}
        animate={{ opacity: 1, y: 0 }}
        transition={{ duration: 0.5 }}
        className="flex items-center justify-between mb-4"
      >
        <h2 className="text-lg font-semibold">Explore Categories</h2>
        <motion.button
          className="text-xs text-primary font-medium"
          whileHover={{ scale: 1.05, x: 3 }}
          whileTap={{ scale: 0.95 }}
        >
          See all →
        </motion.button>
      </motion.div>

      <motion.div 
        className="grid grid-cols-4 gap-3"
        initial={{ opacity: 0 }}
        animate={{ opacity: 1 }}
        transition={{ delay: 0.2 }}
      >
        {categories.map((category, index) => (
          <motion.button
            key={category.label}
            className="flex flex-col items-center gap-2 p-3 rounded-2xl bg-muted/30 border border-border/30 hover:border-primary/30 transition-colors"
            initial={{ opacity: 0, scale: 0.8 }}
            animate={{ opacity: 1, scale: 1 }}
            transition={{ delay: 0.05 * index + 0.3 }}
            whileHover={{ 
              scale: 1.05, 
              y: -5,
              boxShadow: "0 10px 30px oklch(0 0 0 / 0.2)"
            }}
            whileTap={{ scale: 0.95 }}
          >
            <motion.div
              className={`w-12 h-12 rounded-xl bg-gradient-to-br ${category.color} flex items-center justify-center`}
              whileHover={{
                boxShadow: "0 0 20px oklch(0.65 0.18 45 / 0.4)"
              }}
            >
              <category.icon className="w-5 h-5 text-background" />
            </motion.div>
            <div className="text-center">
              <p className="text-xs font-medium">{category.label}</p>
              <p className="text-[10px] text-muted-foreground">{category.count}</p>
            </div>
          </motion.button>
        ))}
      </motion.div>
    </section>
  )
}
