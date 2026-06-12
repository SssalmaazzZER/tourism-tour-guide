"use client"

import { motion } from "framer-motion"
import { Home, Compass, Calendar, Heart, User } from "lucide-react"
import { useState } from "react"

const navItems = [
  { icon: Home, label: "Home", active: true },
  { icon: Compass, label: "Explore", active: false },
  { icon: Calendar, label: "Plan", active: false },
  { icon: Heart, label: "Saved", active: false },
  { icon: User, label: "Profile", active: false },
]

export function BottomNav() {
  const [activeIndex, setActiveIndex] = useState(0)

  return (
    <motion.nav
      initial={{ y: 100, opacity: 0 }}
      animate={{ y: 0, opacity: 1 }}
      transition={{ delay: 0.5, type: "spring", damping: 20 }}
      className="sticky bottom-0 z-50 px-4 py-2 backdrop-blur-xl bg-background/90 border-t border-border/50"
    >
      <div className="flex items-center justify-around">
        {navItems.map((item, index) => (
          <motion.button
            key={item.label}
            className="relative flex flex-col items-center gap-1 px-4 py-2"
            onClick={() => setActiveIndex(index)}
            whileHover={{ scale: 1.1 }}
            whileTap={{ scale: 0.9 }}
          >
            {/* Active indicator */}
            {activeIndex === index && (
              <motion.div
                layoutId="activeTab"
                className="absolute -top-2 w-8 h-1 rounded-full bg-gradient-to-r from-primary via-secondary to-primary"
                initial={{ opacity: 0 }}
                animate={{ opacity: 1 }}
                transition={{ type: "spring", stiffness: 300, damping: 30 }}
              />
            )}
            
            <motion.div
              className={`w-10 h-10 rounded-xl flex items-center justify-center transition-all duration-300 ${
                activeIndex === index 
                  ? "bg-primary/20" 
                  : "bg-transparent"
              }`}
              animate={{
                scale: activeIndex === index ? 1.1 : 1,
              }}
            >
              <item.icon 
                className={`w-5 h-5 transition-colors duration-300 ${
                  activeIndex === index 
                    ? "text-primary" 
                    : "text-muted-foreground"
                }`} 
              />
            </motion.div>
            
            <span 
              className={`text-[10px] font-medium transition-colors duration-300 ${
                activeIndex === index 
                  ? "text-primary" 
                  : "text-muted-foreground"
              }`}
            >
              {item.label}
            </span>

            {/* Glow effect for active item */}
            {activeIndex === index && (
              <motion.div
                className="absolute inset-0 rounded-xl bg-primary/10 blur-lg -z-10"
                initial={{ opacity: 0 }}
                animate={{ opacity: 0.5 }}
                exit={{ opacity: 0 }}
              />
            )}
          </motion.button>
        ))}
      </div>
    </motion.nav>
  )
}
