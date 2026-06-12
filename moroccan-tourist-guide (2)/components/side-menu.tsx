"use client"

import { motion, AnimatePresence } from "framer-motion"
import { 
  X, User, Settings, Heart, MapPin, Clock, 
  Trophy, Utensils, Mountain, Building2, Sun
} from "lucide-react"

interface SideMenuProps {
  isOpen: boolean
  onClose: () => void
}

const menuItems = [
  { icon: User, label: "Profile", color: "text-primary" },
  { icon: Heart, label: "Favorites", color: "text-red-400" },
  { icon: Clock, label: "Recent", color: "text-secondary" },
  { icon: MapPin, label: "Saved Places", color: "text-accent" },
  { icon: Settings, label: "Settings", color: "text-muted-foreground" },
]

const quickLinks = [
  { icon: Trophy, label: "Football", gradient: "from-emerald to-accent" },
  { icon: Utensils, label: "Cuisine", gradient: "from-primary to-secondary" },
  { icon: Mountain, label: "Atlas", gradient: "from-secondary to-primary" },
  { icon: Building2, label: "Medinas", gradient: "from-accent to-emerald" },
  { icon: Sun, label: "Desert", gradient: "from-saffron to-primary" },
]

export function SideMenu({ isOpen, onClose }: SideMenuProps) {
  return (
    <AnimatePresence>
      {isOpen && (
        <>
          {/* Backdrop */}
          <motion.div
            initial={{ opacity: 0 }}
            animate={{ opacity: 1 }}
            exit={{ opacity: 0 }}
            onClick={onClose}
            className="fixed inset-0 bg-background/80 backdrop-blur-sm z-50"
          />

          {/* Menu panel */}
          <motion.div
            initial={{ x: "-100%" }}
            animate={{ x: 0 }}
            exit={{ x: "-100%" }}
            transition={{ type: "spring", damping: 25, stiffness: 200 }}
            className="fixed left-0 top-0 bottom-0 w-[85%] max-w-sm bg-card border-r border-border z-50 overflow-hidden"
          >
            {/* Decorative background */}
            <div className="absolute inset-0 moroccan-gradient opacity-50" />
            
            <div className="relative h-full flex flex-col">
              {/* Header */}
              <div className="p-6 border-b border-border/50">
                <div className="flex items-center justify-between mb-6">
                  <motion.h2 
                    className="text-xl font-bold"
                    initial={{ opacity: 0, x: -20 }}
                    animate={{ opacity: 1, x: 0 }}
                    transition={{ delay: 0.2 }}
                  >
                    Menu
                  </motion.h2>
                  <motion.button
                    onClick={onClose}
                    className="w-10 h-10 rounded-xl bg-muted/50 flex items-center justify-center"
                    whileHover={{ scale: 1.1, rotate: 90 }}
                    whileTap={{ scale: 0.9 }}
                  >
                    <X className="w-5 h-5" />
                  </motion.button>
                </div>

                {/* User profile */}
                <motion.div
                  className="flex items-center gap-4 p-4 rounded-2xl bg-muted/30 border border-border/50"
                  initial={{ opacity: 0, y: 20 }}
                  animate={{ opacity: 1, y: 0 }}
                  transition={{ delay: 0.3 }}
                >
                  <div className="w-14 h-14 rounded-xl bg-gradient-to-br from-primary to-accent flex items-center justify-center">
                    <User className="w-7 h-7 text-background" />
                  </div>
                  <div>
                    <p className="font-semibold">Traveler</p>
                    <p className="text-sm text-muted-foreground">Exploring Morocco</p>
                  </div>
                </motion.div>
              </div>

              {/* Menu items */}
              <div className="flex-1 p-6 space-y-2 overflow-y-auto">
                {menuItems.map((item, index) => (
                  <motion.button
                    key={item.label}
                    className="w-full flex items-center gap-4 p-4 rounded-xl hover:bg-muted/50 transition-colors"
                    initial={{ opacity: 0, x: -30 }}
                    animate={{ opacity: 1, x: 0 }}
                    transition={{ delay: 0.1 * index + 0.4 }}
                    whileHover={{ x: 8 }}
                    whileTap={{ scale: 0.98 }}
                  >
                    <item.icon className={`w-5 h-5 ${item.color}`} />
                    <span className="font-medium">{item.label}</span>
                  </motion.button>
                ))}

                {/* Quick access */}
                <motion.div
                  className="pt-6"
                  initial={{ opacity: 0 }}
                  animate={{ opacity: 1 }}
                  transition={{ delay: 0.8 }}
                >
                  <p className="text-xs text-muted-foreground uppercase tracking-wider mb-4">
                    Quick Access
                  </p>
                  <div className="grid grid-cols-3 gap-3">
                    {quickLinks.map((link, index) => (
                      <motion.button
                        key={link.label}
                        className={`flex flex-col items-center gap-2 p-3 rounded-xl bg-gradient-to-br ${link.gradient} opacity-90`}
                        initial={{ opacity: 0, scale: 0.8 }}
                        animate={{ opacity: 0.9, scale: 1 }}
                        transition={{ delay: 0.1 * index + 0.9 }}
                        whileHover={{ scale: 1.05, opacity: 1 }}
                        whileTap={{ scale: 0.95 }}
                      >
                        <link.icon className="w-5 h-5 text-background" />
                        <span className="text-[10px] font-medium text-background">
                          {link.label}
                        </span>
                      </motion.button>
                    ))}
                  </div>
                </motion.div>
              </div>

              {/* Footer */}
              <motion.div
                className="p-6 border-t border-border/50"
                initial={{ opacity: 0 }}
                animate={{ opacity: 1 }}
                transition={{ delay: 1.2 }}
              >
                <p className="text-xs text-center text-muted-foreground">
                  Maghrib Guide v1.0 | Made with ❤️ in Morocco
                </p>
              </motion.div>
            </div>
          </motion.div>
        </>
      )}
    </AnimatePresence>
  )
}
