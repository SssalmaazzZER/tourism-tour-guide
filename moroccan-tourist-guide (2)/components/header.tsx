"use client"

import { motion } from "framer-motion"
import { MapPin, Compass, Star, Menu } from "lucide-react"

interface HeaderProps {
  onMenuClick: () => void
}

export function Header({ onMenuClick }: HeaderProps) {
  return (
    <motion.header
      initial={{ y: -50, opacity: 0 }}
      animate={{ y: 0, opacity: 1 }}
      transition={{ duration: 0.6, ease: "easeOut" }}
      className="sticky top-0 z-50 px-4 py-3 backdrop-blur-xl bg-background/80 border-b border-border/50"
    >
      <div className="flex items-center justify-between">
        {/* Logo */}
        <motion.div
          className="flex items-center gap-2"
          whileHover={{ scale: 1.02 }}
          whileTap={{ scale: 0.98 }}
        >
          <div className="relative">
            <motion.div
              className="w-10 h-10 rounded-xl bg-gradient-to-br from-primary via-secondary to-accent flex items-center justify-center"
              animate={{
                boxShadow: [
                  "0 0 20px oklch(0.65 0.18 45 / 0.3)",
                  "0 0 30px oklch(0.65 0.18 45 / 0.5)",
                  "0 0 20px oklch(0.65 0.18 45 / 0.3)",
                ],
              }}
              transition={{ duration: 2, repeat: Infinity }}
            >
              <Compass className="w-5 h-5 text-background" />
            </motion.div>
            {/* Moroccan star accent */}
            <motion.div
              className="absolute -top-1 -right-1 w-3 h-3"
              animate={{ rotate: 360 }}
              transition={{ duration: 20, repeat: Infinity, ease: "linear" }}
            >
              <Star className="w-full h-full text-secondary fill-secondary" />
            </motion.div>
          </div>
          <div className="flex flex-col">
            <span className="text-lg font-bold bg-gradient-to-r from-primary via-secondary to-primary bg-clip-text text-transparent">
              Maghrib
            </span>
            <span className="text-[10px] text-muted-foreground -mt-1 tracking-wider">
              GUIDE
            </span>
          </div>
        </motion.div>

        {/* Location indicator */}
        <motion.div
          className="flex items-center gap-1.5 px-3 py-1.5 rounded-full bg-muted/50 border border-border/50"
          initial={{ scale: 0.9, opacity: 0 }}
          animate={{ scale: 1, opacity: 1 }}
          transition={{ delay: 0.3 }}
        >
          <motion.div
            animate={{ scale: [1, 1.2, 1] }}
            transition={{ duration: 2, repeat: Infinity }}
          >
            <MapPin className="w-3.5 h-3.5 text-primary" />
          </motion.div>
          <span className="text-xs font-medium">Marrakech</span>
        </motion.div>

        {/* Menu button */}
        <motion.button
          onClick={onMenuClick}
          className="w-10 h-10 rounded-xl bg-muted/50 border border-border/50 flex items-center justify-center"
          whileHover={{ scale: 1.05, backgroundColor: "oklch(0.30 0.03 45)" }}
          whileTap={{ scale: 0.95 }}
        >
          <Menu className="w-5 h-5 text-foreground" />
        </motion.button>
      </div>
    </motion.header>
  )
}
